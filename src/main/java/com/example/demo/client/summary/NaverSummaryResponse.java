package com.example.demo.client.summary;

import com.example.demo.domain.dto.request.NaverSummaryRequest;
import com.example.demo.domain.dto.response.NaverSummaryResponseDto;
import com.example.demo.web.dto.request.GptMessageRequest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class NaverSummaryResponse implements SummaryResponse {

    @Override
    public String getResponse(String message) {
        String response = connect(message);


        return response;
    }

    private String connect(String message) {
        String url = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";

        NaverSummaryRequest requestDto = new NaverSummaryRequest();
        requestDto.setMessage(message);

        // RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 매개변수 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-NCP-APIGW-API-KEY-ID","s0pzoix1k1" );
        headers.add("X-NCP-APIGW-API-KEY", "PdLSWdaLB1OaxGD4u7ir2IWUTvl8IGKCsj2y8Ysp");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NaverSummaryRequest> request = new HttpEntity<>(requestDto, headers);
        System.out.println(request.toString());

        // HTTP 요청 및 응답 처리
        ResponseEntity<NaverSummaryResponseDto> responseDto =
                restTemplate.exchange(url, HttpMethod.POST, request, NaverSummaryResponseDto.class);

        return responseDto.getBody().getSummary();
    }


}
