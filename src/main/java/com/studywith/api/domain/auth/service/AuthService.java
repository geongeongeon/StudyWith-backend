package com.studywith.api.domain.auth.service;

import com.studywith.api.domain.auth.exception.InvalidTokenException;
import com.studywith.api.domain.auth.exception.LogoutFailedException;
import com.studywith.api.global.redis.RedisService;
import com.studywith.api.global.security.OAuth2TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisService redisService;
    private final OAuth2TokenProvider oAuth2TokenProvider;

    public String getNewAccessToken(String loginId, String registrationId, String refreshToken) {
        String refreshTokenInRedis = redisService.getRefreshToken(loginId);
        if (refreshTokenInRedis == null || refreshToken == null) {
            throw new InvalidTokenException("인증 정보가 만료되었습니다.");
        } else if (!refreshToken.equals(refreshTokenInRedis)) {
            throw new InvalidTokenException("잘못된 인증 정보입니다.");
        }

        String accessToken = redisService.getAccessToken(loginId);

        return (accessToken == null) ? oAuth2TokenProvider.getAccessToken(registrationId, refreshToken) : accessToken;
    }

    public void revokeAccessToken(String registrationId, String accessToken) {
        boolean isRevoked = oAuth2TokenProvider.revokeAccessToken(registrationId, accessToken);
        if (!isRevoked) {
            throw new LogoutFailedException("로그아웃 요청이 유효하지 않습니다.");
        }
    }
}
