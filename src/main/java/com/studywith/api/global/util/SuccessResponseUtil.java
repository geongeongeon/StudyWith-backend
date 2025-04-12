package com.studywith.api.global.util;

import com.studywith.api.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessResponseUtil {

    /** 요청이 성공했을 때 */
    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(200, message, data));
    }

    /** 리소스가 생성됐을 때 */
    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, message, data));
    }

}
