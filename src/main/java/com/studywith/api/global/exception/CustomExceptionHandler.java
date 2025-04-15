package com.studywith.api.global.exception;

import com.studywith.api.domain.member.exception.MemberNicknameAlreadyInUseException;
import com.studywith.api.domain.member.exception.MemberNoChangesException;
import com.studywith.api.domain.member.exception.MemberNotFoundException;
import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.FailureResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MemberNicknameAlreadyInUseException.class)
    public ResponseEntity<ApiResponse<Object>> memberNicknameAlreadyInUseExceptionHandler(MemberNicknameAlreadyInUseException e) {
        return FailureResponseUtil.conflict(e.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> memberNotFoundExceptionHandler(MemberNotFoundException e) {
        return FailureResponseUtil.notFound(e.getMessage());
    }

    @ExceptionHandler(MemberNoChangesException.class)
    public ResponseEntity<ApiResponse<Object>> memberNoChangesExceptionHandler(MemberNoChangesException e) {
        return FailureResponseUtil.badRequest(e.getMessage());
    }

}
