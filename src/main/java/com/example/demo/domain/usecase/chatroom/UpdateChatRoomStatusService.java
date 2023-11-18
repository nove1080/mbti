package com.example.demo.domain.usecase.chatroom;

import com.example.demo.repository.entity.ChatRoomListEntity;
import com.example.demo.repository.entity.constant.ChatRoomStatus;
import com.example.demo.repository.repository.ChatRoomListRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UpdateChatRoomStatusService {

	private final ChatRoomListRepository chatRoomListRepository;

	/** 특정 멤버에 대한 채팅방의 상태를 변경 */
	@Transactional
	public void updateChatStatus(Long memberId, Long chatroomId, ChatRoomStatus status) {
		chatRoomListRepository
				.findByMemberIdAndChatRoomId(memberId, chatroomId)
				.ifPresent((c) -> c.changeChatStatus(status));
	}

	@Transactional
	public void updateAllComplete(Long chatroomId) {
		List<ChatRoomListEntity> chatrooms = chatRoomListRepository.findAllByChatRoomId(chatroomId);
		for (ChatRoomListEntity chatroom : chatrooms) {
			chatroom.changeChatStatus(ChatRoomStatus.COMPLETE);
		}
	}

	public boolean checkComplete(Long chatroomId) {
		List<ChatRoomListEntity> chatrooms = chatRoomListRepository.findAllByChatRoomId(chatroomId);
		for (ChatRoomListEntity chatroom : chatrooms) {
			if (chatroom.getChatStatus().equals(ChatRoomStatus.SURVEY)) return false;
		}
		return true;
	}
}
