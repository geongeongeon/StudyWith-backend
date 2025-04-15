package com.studywith.api.global.exception;

import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.FailureResponseUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Object>> nullPointerExceptionHandler() {
        return FailureResponseUtil.badRequest("일부 필드가 누락되었습니다.");
    }

    @ExceptionHandler({IllegalArgumentException.class, DateTimeParseException.class})
    public ResponseEntity<ApiResponse<Object>> illegalArgumentExceptionAndDateTimeParseExceptionHandler() {
        return FailureResponseUtil.badRequest("잘못된 인자가 제공되었습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> methodArgumentNotValidExceptionHandler() {
        return FailureResponseUtil.badRequest("유효성 검사가 실패했습니다.");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> authenticationExceptionHandler() {
        return FailureResponseUtil.unauthorized("인증되지 않은 사용자입니다.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> accessDeniedExceptionHandler() {
        return FailureResponseUtil.forbidden("접근할 수 있는 권한이 없습니다.");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> entityNotFoundExceptionHandler() {
        return FailureResponseUtil.notFound("리소스를 찾을 수 없습니다.");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> noHandlerFoundExceptionHandler() {
        return FailureResponseUtil.notFound("경로를 찾을 수 없습니다.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> dataIntegrityViolationExceptionHandler() {
        return FailureResponseUtil.conflict("데이터 무결성 위반 오류가 발생했습니다.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> exceptionHandler() {
        return FailureResponseUtil.internalServerError("알 수 없는 오류가 발생했습니다.");
    }

}
