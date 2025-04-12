package com.studywith.api.domain;

import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.SuccessResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/connection")
public class ConnectionTestController {

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<Void>> test() {
        System.out.println("Successfully connected to frontend.");

        return SuccessResponseUtil.ok("Successfully connected to backend.", null);
    }

}
