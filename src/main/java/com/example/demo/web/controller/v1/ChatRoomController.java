package com.example.demo.web.controller.v1;

import com.example.demo.domain.usecase.chatroom.CreateChatRoomListService;
import com.example.demo.domain.usecase.chatroom.CreateChatRoomService;
import com.example.demo.web.dto.request.CreateChatRoomRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatrooms")
public class ChatRoomController {

	private final CreateChatRoomService createChatRoomService;
	private final CreateChatRoomListService createChatRoomListService;

	@PostMapping
	public String save(@RequestBody @Valid CreateChatRoomRequest request) {
		Long memberId = 1L;
		Long chatroomId = createChatRoomService.execute(request);
		createChatRoomListService.create(memberId, chatroomId);
		return "redirect:/api/v1/chatrooms";
	}
}
