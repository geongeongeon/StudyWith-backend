package com.studywith.api.domain.message.exception;

import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.FailureResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MessageExceptionHandler {

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> messageNotFoundExceptionHandler(MessageNotFoundException e) {
        return FailureResponseUtil.badRequest(e.getMessage());
    }

    @ExceptionHandler(MessageAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> messageAccessDeniedExceptionHandler(MessageAccessDeniedException e) {
        return FailureResponseUtil.forbidden(e.getMessage());
    }

}
