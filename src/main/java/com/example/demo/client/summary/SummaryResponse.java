package com.example.demo.client.summary;

import com.example.demo.web.dto.request.GptMessageRequest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public interface SummaryResponse {

	String getResponse(String message);

	private String connect(String message) {
		String url = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";

		GptMessageRequest requestDto = GptMessageRequest.builder().data(message).build();

		// RestTemplate 생성
		RestTemplate restTemplate = new RestTemplate();

		// 요청 매개변수 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GptMessageRequest> request = new HttpEntity<>(requestDto, headers);

		// HTTP 요청 및 응답 처리
		ResponseEntity<String> responseDto =
				restTemplate.exchange(url, HttpMethod.POST, request, String.class);

		return responseDto.getBody();
	}
}
