package com.studywith.api.domain.auth.controller;

import com.studywith.api.domain.auth.service.AuthService;
import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.ResponseHeaderUtil;
import com.studywith.api.global.util.SuccessResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponse<Void>> refresh(
            HttpServletResponse response, @CookieValue("REGISTRATION_ID") String registrationId, @CookieValue(name = "REFRESH_TOKEN", required = false) String refreshToken
    ) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        String newAccessToken = authService.getNewAccessToken(loginId, registrationId, refreshToken);
        ResponseHeaderUtil.setAuthorization(response, "Bearer " + newAccessToken);

        return SuccessResponseUtil.ok("액세스 토큰이 재발급되었습니다.", null);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            HttpServletResponse response, @CookieValue("REGISTRATION_ID") String registrationId, @RequestHeader("Authorization") String authorizationHeader
    ) {
        String accessToken = authorizationHeader.substring("Bearer ".length());
        authService.revokeAccessToken(registrationId, accessToken);
        ResponseHeaderUtil.setCookie(response, "REFRESH_TOKEN", "", 0L);
        ResponseHeaderUtil.setCookie(response, "REGISTRATION_ID", "", 0L);

        return SuccessResponseUtil.ok("로그아웃이 완료되었습니다.", null);
    }

}
