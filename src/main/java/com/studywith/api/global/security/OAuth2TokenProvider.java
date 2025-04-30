package com.studywith.api.global.security;

import com.studywith.api.domain.auth.exception.UnsupportedSocialLoginException;
import com.studywith.api.global.external.GoogleOAuth2Service;
import com.studywith.api.global.external.KakaoOAuth2Service;
import com.studywith.api.global.external.NaverOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2TokenProvider {

    private final GoogleOAuth2Service googleOAuth2Service;
    private final NaverOAuth2Service naverOAuth2Service;
    private final KakaoOAuth2Service kakaoOAuth2Service;

    public String getAccessToken(String registrationId, String refreshToken) {
        return switch (registrationId) {
            case "google" -> googleOAuth2Service.getAccessToken(refreshToken);
            case "naver" -> naverOAuth2Service.getAccessToken(refreshToken);
            case "kakao" -> kakaoOAuth2Service.getAccessToken(refreshToken);
            default -> throw new UnsupportedSocialLoginException("지원하지 않는 소셜 로그인입니다.");
        };
    }

    public Authentication getAuthentication(String registrationId, String accessToken) {
        return switch (registrationId) {
            case "google" -> googleOAuth2Service.getAuthentication(accessToken);
            case "naver" -> naverOAuth2Service.getAuthentication(accessToken);
            case "kakao" -> kakaoOAuth2Service.getAuthentication(accessToken);
            default -> throw new UnsupportedSocialLoginException("지원하지 않는 소셜 로그인입니다.");
        };
    }

    public boolean validateAccessToken(String registrationId, String accessToken) {
        return switch (registrationId) {
            case "google" -> googleOAuth2Service.validateAccessToken(accessToken);
            case "naver" -> naverOAuth2Service.validateAccessToken(accessToken);
            case "kakao" -> kakaoOAuth2Service.validateAccessToken(accessToken);
            default -> throw new UnsupportedSocialLoginException("지원하지 않는 소셜 로그인입니다.");
        };
    }

    public boolean revokeAccessToken(String registrationId, String accessToken) {
        return switch (registrationId) {
            case "google" -> googleOAuth2Service.revokeAccessToken(accessToken);
            case "naver" -> naverOAuth2Service.revokeAccessToken(accessToken);
            case "kakao" -> kakaoOAuth2Service.revokeAccessToken(accessToken);
            default -> throw new UnsupportedSocialLoginException("지원하지 않는 소셜 로그인입니다.");
        };
    }

}
