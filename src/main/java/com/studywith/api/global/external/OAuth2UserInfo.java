package com.studywith.api.global.external;

import com.studywith.api.domain.auth.exception.UnsupportedSocialLoginException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class OAuth2UserInfo {

    private String loginId;
    private String email;
    private String accountType;

    public static OAuth2UserInfo from(String registrationId,  Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> ofGoogle(attributes);
            default -> throw new UnsupportedSocialLoginException("지원하지 않는 소셜 로그인입니다.");
            };
        }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        String loginId = "google_%s".formatted(attributes.get("sub").toString());
        return new OAuth2UserInfo(loginId, attributes.get("email").toString(), "GOOGLE");
    }

}
