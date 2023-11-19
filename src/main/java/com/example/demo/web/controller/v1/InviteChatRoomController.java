package com.example.demo.web.controller.v1;

import com.example.demo.domain.usecase.chatroom.InviteChatRoomService;
import com.example.demo.security.authentication.token.TokenUserDetailsService;
import com.example.demo.support.ApiResponse;
import com.example.demo.web.dto.request.InviteChatRoomRequest;
import com.example.demo.web.dto.response.ChatRoomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatrooms")
public class InviteChatRoomController {

    private final TokenUserDetailsService tokenUserDetailsService;
    private final InviteChatRoomService inviteChatRoomService;

    @PostMapping("{chatRoomId}/invite")
    public String invite(@PathVariable Long chatRoomId, @RequestBody String password, HttpServletRequest request) {
        Long memberId = findMemberByToken(request);
        InviteChatRoomRequest inviteChatRoomRequest = InviteChatRoomRequest.builder()
                .chatRoomId(chatRoomId)
                .memberId(memberId)
                .password(password).build();

        inviteChatRoomService.execute(inviteChatRoomRequest);

        return "redirect:/api/v1/chatrooms";
    }

    private Long findMemberByToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String substring = authorization.substring(7, authorization.length());
        UserDetails userDetails = tokenUserDetailsService.loadUserByUsername(substring);
        Long memberId = Long.parseLong(userDetails.getUsername());
        return memberId;
    }

}
