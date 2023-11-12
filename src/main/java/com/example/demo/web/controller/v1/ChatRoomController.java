package com.example.demo.web.controller.v1;

import com.example.demo.domain.usecase.chatroom.CreateChatRoomListService;
import com.example.demo.domain.usecase.chatroom.CreateChatRoomService;
import com.example.demo.domain.usecase.chatroom.ReadChatRoomService;
import com.example.demo.domain.usecase.chatroom.ReadChatSurveyQuestionService;
import com.example.demo.support.ApiResponse;
import com.example.demo.support.ApiResponseGenerator;
import com.example.demo.web.dto.request.CreateChatRoomRequest;
import com.example.demo.web.dto.response.ChatRoomResponse;
import com.example.demo.web.dto.response.ChatSurveyQuestionResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatrooms")
public class ChatRoomController {

	private final CreateChatRoomService createChatRoomService;
	private final ReadChatRoomService readChatRoomService;
	private final CreateChatRoomListService createChatRoomListService;

	@PostMapping
	public String save(@RequestBody @Valid CreateChatRoomRequest request) {
		Long memberId = 1L;
		Long chatroomId = createChatRoomService.execute(request);
		createChatRoomListService.create(1L, chatroomId);
		return "redirect:/api/v1/chatrooms";
	}

	@GetMapping
	public ApiResponse<ApiResponse.SuccessBody<ChatRoomResponse>> loadChatRooms() {
		Long memberId = 1L;
		ChatRoomResponse res = readChatRoomService.execute(memberId);
		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}

}
