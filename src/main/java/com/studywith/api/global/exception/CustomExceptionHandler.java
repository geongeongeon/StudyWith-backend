package com.studywith.api.global.exception;

import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.FailureResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NicknameAlreadyInUseException.class)
    public ResponseEntity<ApiResponse<Object>> nicknameAlreadyInUseExceptionHandler(NicknameAlreadyInUseException e) {
        return FailureResponseUtil.conflict(e.getMessage());
    }

}
