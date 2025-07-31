package com.studywith.api.domain.study.exception;

import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.FailureResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StudyExceptionHandler {

    @ExceptionHandler(StudyNameAlreadyInUseException.class)
    public ResponseEntity<ApiResponse<Object>> studyNameAlreadyInUseExceptionHandler(StudyNameAlreadyInUseException e) {
        return FailureResponseUtil.conflict(e.getMessage());
    }

    @ExceptionHandler(StudyJoinRequestAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> studyJoinRequestAlreadyExistsExceptionHandler(StudyJoinRequestAlreadyExistsException e) {
        return FailureResponseUtil.conflict(e.getMessage());
    }

    @ExceptionHandler(StudySubMangerAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> studySubMangerAlreadyExistsExceptionHandler(StudySubMangerAlreadyExistsException e) {
        return FailureResponseUtil.conflict(e.getMessage());
    }

    @ExceptionHandler(StudyNotRecruitException.class)
    public ResponseEntity<ApiResponse<Object>> studyNotRecruitExceptionHandler(StudyNotRecruitException e) {
        return FailureResponseUtil.conflict(e.getMessage());
    }

    @ExceptionHandler(StudyMemberAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> studyMemberAlreadyExistsExceptionHandler(StudyMemberAlreadyExistsException e) {
        return FailureResponseUtil.conflict(e.getMessage());
    }

    @ExceptionHandler(StudyNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> studyNotFoundExceptionHandler(StudyNotFoundException e) {
        return FailureResponseUtil.notFound(e.getMessage());
    }

    @ExceptionHandler(StudyMemberNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> studyMemberNotFoundExceptionHandler(StudyMemberNotFoundException e) {
        return FailureResponseUtil.notFound(e.getMessage());
    }

    @ExceptionHandler(StudySubManagerNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> studySubMangerNotFoundExceptionHandler(StudySubManagerNotFoundException e) {
        return FailureResponseUtil.notFound(e.getMessage());
    }

    @ExceptionHandler(StudyAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> studyAccessDeniedExceptionHandler(StudyAccessDeniedException e) {
        return FailureResponseUtil.forbidden(e.getMessage());
    }

    @ExceptionHandler(StudyHasOtherMembersException.class)
    public ResponseEntity<ApiResponse<Object>> studyHasOtherMembersExceptionHandler(StudyHasOtherMembersException e) {
        return FailureResponseUtil.conflict(e.getMessage());
    }

}
