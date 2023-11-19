package com.example.demo.domain.usecase.chatroom;

import com.example.demo.client.gpt.AiResponse;
import com.example.demo.client.gpt.GptAiResponse;
import com.example.demo.domain.dto.request.VoteDestinationDomainRequest;
import com.example.demo.domain.model.VotePaper;
import com.example.demo.repository.entity.ChatRoomListEntity;
import com.example.demo.repository.entity.SpotEntity;
import com.example.demo.repository.repository.ChatRoomListRepository;
import com.example.demo.repository.repository.SpotRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteDestinationService {

	private final double SELECT_RATIO = 0.66;

	private static final Map<Long, VotePaper> AllBallotPapers =
			new ConcurrentHashMap<>(); // chatRoomId, 투표용지

	private final ChatRoomListRepository chatRoomListRepository;

	private final SpotRepository spotRepository;

	public void execute(VoteDestinationDomainRequest request) {
		if (!AllBallotPapers.containsKey(request.getChatRoomId())) {
			AllBallotPapers.put(
					request.getChatRoomId(), VotePaper.builder().paper(new ConcurrentHashMap<>()).build());
		}
		VotePaper votePaper = AllBallotPapers.get(request.getChatRoomId());

		votePaper.put(request.getMemberId(), request.getVoteResult());
		List<ChatRoomListEntity> allByChatRoomId =
				chatRoomListRepository.findAllByChatRoomId(request.getChatRoomId());

		// 모두가 투표완료인 경우
		if (votePaper.getSize() == allByChatRoomId.size()) {
			List<Integer> shouldModifiedSpot = compareVote(votePaper, allByChatRoomId.size());
			// todo
			// 문항번호를 다시 여행지로 변환해줘야함
			List<SpotEntity> allSpotByChatRoomId =
					spotRepository.findAllByChatRoomId(request.getChatRoomId());
			List<SpotEntity> shouldModifiedSpotEntity = new ArrayList<>();

			for (int i = 0; i < shouldModifiedSpot.size(); i++) {
				shouldModifiedSpotEntity.add(allSpotByChatRoomId.get(shouldModifiedSpot.get(i)));
			}
			List<String> shouldModifiedSpotName =
					shouldModifiedSpotEntity.stream()
							.map(e -> e.getSpot())
							.collect(Collectors.toCollection(ArrayList::new));

			// firebase로 채팅 내용 불러오고

			// gpt를 통해 수정된 내용을 받아오고 채팅내용과 함께 gpt로 전송
			// todo prompt 추가
			AiResponse ai = new GptAiResponse();
			String gptResponse =
					ai.getResponse(
							String.join(",", shouldModifiedSpotName)
									+ " 의 제주도 여행지를 다른걸로 추천해줘 이때 다른 말은 하지 말고 ','로 여행지만 분리해줘");

			String[] splitGptResponse = gptResponse.split(",");
			for (int i = 0; i < splitGptResponse.length; i++) {
				SpotEntity spotEntity = shouldModifiedSpotEntity.get(i);
				spotEntity.changeSpot(splitGptResponse[i]);
			}

			//            //FirebaseClient로 전송
			//            //todo prompt추가
			//            FirebaseClient firebaseClient = new FirebaseClient();
			//            firebaseClient.postRequest(request.getChatRoomId(), gptResponse);
			//
			//
			log.debug("getResponse : {}", gptResponse);
			votePaper.reset();
		}
	}

	private List<Integer> compareVote(VotePaper chatRoomBallotPaper, int memberCount) {
		List<Integer> shouldModifiedSpot = new ArrayList<>();
		HashMap<Integer, Integer> paperCount = chatRoomBallotPaper.getPaperCount();
		int SelectSpotLimit = (int) Math.floor(memberCount * SELECT_RATIO);

		for (Integer spotId : paperCount.keySet()) {
			if (paperCount.get(spotId) < SelectSpotLimit) {
				shouldModifiedSpot.add(spotId);
			}
		}
		return shouldModifiedSpot;
	}
}
