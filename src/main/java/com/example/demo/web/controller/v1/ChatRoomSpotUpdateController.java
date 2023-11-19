package com.example.demo.web.controller.v1;

import com.example.demo.domain.dto.request.VoteDestinationDomainRequest;
import com.example.demo.domain.usecase.chatroom.VoteDestinationService;
import com.example.demo.web.dto.request.VoteDestinationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatrooms")
public class ChatRoomSpotUpdateController {

    private final VoteDestinationService voteDestinationService;

    @PostMapping("{chatRoomId}/agree/{memberId}")
    public void spotUpdate(@PathVariable Long chatRoomId, @PathVariable Long memberId, @RequestBody VoteDestinationRequest request) {
        voteDestinationService.execute(VoteDestinationDomainRequest.builder()
                .chatRoomId(chatRoomId)
                .memberId(memberId)
                .voteResult(request.getVoteResult())
                .build());


    }

}
