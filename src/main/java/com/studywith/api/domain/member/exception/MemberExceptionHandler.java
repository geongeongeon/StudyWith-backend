package com.studywith.api.domain.member.exception;

import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.FailureResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberNicknameAlreadyInUseException.class)
    public ResponseEntity<ApiResponse<Object>> memberNicknameAlreadyInUseExceptionHandler(MemberNicknameAlreadyInUseException e) {
        return FailureResponseUtil.conflict(e.getMessage());
    }

    @ExceptionHandler(MemberIsManagingStudyException.class)
    public ResponseEntity<ApiResponse<Object>> memberIsManagingStudyExceptionHandler(MemberIsManagingStudyException e) {
        return FailureResponseUtil.conflict(e.getMessage());
    }

    @ExceptionHandler(MemberTempNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> memberTempNotFoundExceptionHandler(MemberTempNotFoundException e) {
        return FailureResponseUtil.badRequest(e.getMessage());
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
