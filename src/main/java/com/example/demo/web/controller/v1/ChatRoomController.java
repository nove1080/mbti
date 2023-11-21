package com.example.demo.web.controller.v1;

import com.example.demo.domain.usecase.chatroom.*;
import com.example.demo.domain.usecase.spot.ReadSpotService;
import com.example.demo.repository.entity.constant.ChatRoomStatus;
import com.example.demo.security.authentication.token.TokenUserDetailsService;
import com.example.demo.support.ApiResponse;
import com.example.demo.support.ApiResponseGenerator;
import com.example.demo.web.dto.request.CreateChatRoomRequest;
import com.example.demo.web.dto.request.CreateChatSurveyResultRequest;
import com.example.demo.web.dto.response.ChatRoomResponse;
import com.example.demo.web.dto.response.ChatSurveyQuestionResponse;
import com.example.demo.web.dto.response.SpotResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatrooms")
public class ChatRoomController {

	private final CreateChatRoomService createChatRoomService;
	private final ReadChatRoomService readChatRoomService;
	private final ReadChatSurveyQuestionService readChatSurveyQuestionService;
	private final CreateChatRoomListService createChatRoomListService;
	private final CreateChatSurveyResultService createChatSurveyResultService;
	private final UpdateChatRoomStatusService updateChatRoomStatusService;
	private final ReadChatRoomTitleService readChatRoomTitleService;
	private final ReadSpotService readSpotService;

	private final TokenUserDetailsService tokenUserDetailsService;

	@PostMapping
	public String save(
			@RequestBody @Valid CreateChatRoomRequest requestData, HttpServletRequest request) {
		Long memberId = findMemberByToken(request);
		Long chatroomId = createChatRoomService.execute(requestData,memberId);

		createChatRoomListService.create(memberId, chatroomId);
		return "redirect:/api/v1/chatrooms";
	}

	@GetMapping
	public ApiResponse<ApiResponse.SuccessBody<ChatRoomResponse>> loadChatRooms(
			HttpServletRequest request) {
		Long memberId = findMemberByToken(request);
		ChatRoomResponse res = readChatRoomService.execute(memberId);
		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}

	@GetMapping("/survey/{version}")
	public ApiResponse<ApiResponse.SuccessBody<ChatSurveyQuestionResponse>> loadSurvey(
			@PathVariable double version) {
		ChatSurveyQuestionResponse res = readChatSurveyQuestionService.execute(version);
		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}

	// TODO: @RequestBody 제거
	@PostMapping("/survey")
	public String submitSurvey(
			@RequestBody CreateChatSurveyResultRequest requestData, HttpServletRequest request) {
		createChatSurveyResultService.execute(requestData);
		changeChatroomStatus(requestData, request);
		return "redirect:/api/v1/chatrooms";
	}

	@GetMapping("/{chatroomId}/title")
	public String getChatRoomTitle(@PathVariable Long chatroomId) {
		return readChatRoomTitleService.execute(chatroomId);
	}

	@GetMapping("/{chatroomId}/recommendation")
	public ApiResponse<ApiResponse.SuccessBody<SpotResponse>> getRecommendedSpot(
			@PathVariable Long chatroomId) {
		SpotResponse res = readSpotService.execute(chatroomId);
		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}

	private void changeChatroomStatus(
			CreateChatSurveyResultRequest requestData, HttpServletRequest request) {
		updateChatRoomStatusService.updateChatStatus(
				findMemberByToken(request), requestData.getChatRoomId(), ChatRoomStatus.WAITING);
		if (updateChatRoomStatusService.checkComplete(requestData.getChatRoomId())) {
			// TODO: 여기에 gpt가 여행지 추천을 한 번 해줘야 한다.
			updateChatRoomStatusService.updateAllComplete(requestData.getChatRoomId());
		}
	}

	private Long findMemberByToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		String substring = authorization.substring(7, authorization.length());
		UserDetails userDetails = tokenUserDetailsService.loadUserByUsername(substring);
		Long memberId = Long.parseLong(userDetails.getUsername());
		return memberId;
	}
}
