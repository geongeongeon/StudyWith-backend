package com.studywith.api.domain.message.controller;

import com.studywith.api.domain.message.dto.MessageDetailDTO;
import com.studywith.api.domain.message.dto.MessageSendDTO;
import com.studywith.api.domain.message.dto.MessageSummaryDTO;
import com.studywith.api.domain.message.service.MessageService;
import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.SuccessResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<MessageSendDTO>> sendMessage(@RequestBody MessageSendDTO messageSendDTO) {
        System.out.println(messageSendDTO.toString());
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        MessageSendDTO message = messageService.createMessage(loginId, messageSendDTO);

        return SuccessResponseUtil.created("메시지가 성공적으로 전송되었습니다.", message);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MessageSummaryDTO>>> getMessages() {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MessageSummaryDTO> messages = messageService.getMessages(loginId);

        return SuccessResponseUtil.ok("메시지 목록을 성공적으로 조회했습니다.", messages);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<ApiResponse<MessageDetailDTO>> getMessageDetail(@PathVariable("messageId") Long messageId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        MessageDetailDTO message = messageService.getMessage(loginId, messageId);

        return SuccessResponseUtil.ok("메시지를 성공적으로 조회했습니다.", message);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteAllMessages() {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        messageService.deleteAllMessages(loginId);

        return SuccessResponseUtil.ok("모든 메시지가 성공적으로 삭제되었습니다.", null);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable("messageId") Long messageId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        messageService.deleteMessage(loginId, messageId);

        return SuccessResponseUtil.ok("메시지가 성공적으로 삭제되었습니다.", null);
    }

}
