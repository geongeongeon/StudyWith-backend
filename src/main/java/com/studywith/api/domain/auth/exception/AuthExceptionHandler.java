package com.studywith.api.domain.auth.exception;

import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.FailureResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<Object>> invalidTokenExceptionHandler(InvalidTokenException e) {
        return FailureResponseUtil.unauthorized(e.getMessage());
    }

    @ExceptionHandler(LogoutFailedException.class)
    public ResponseEntity<ApiResponse<Object>> logoutFailedExceptionHandler(LogoutFailedException e) {
        return FailureResponseUtil.badRequest(e.getMessage());
    }

    @ExceptionHandler(UnsupportedSocialLoginException.class)
    public ResponseEntity<ApiResponse<Object>> unsupportedSocialLoginExceptionHandler(UnsupportedSocialLoginException e) {
        return FailureResponseUtil.badRequest(e.getMessage());
    }

}
