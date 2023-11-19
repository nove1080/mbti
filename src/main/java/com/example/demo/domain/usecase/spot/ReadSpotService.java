package com.example.demo.domain.usecase.spot;

import com.example.demo.repository.repository.SpotRepository;
import com.example.demo.web.dto.response.SpotResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadSpotService {

	private final SpotRepository spotRepository;

	@Transactional(readOnly = true)
	public SpotResponse execute(Long chatroomId) {
		List<String> spots = new ArrayList<>();
		spotRepository.findAllByChatRoomId(chatroomId).forEach(spot -> spots.add(spot.getSpot()));

		return SpotResponse.builder().spots(spots).build();
	}
}
