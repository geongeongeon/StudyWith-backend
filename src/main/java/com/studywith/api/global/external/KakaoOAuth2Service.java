package com.studywith.api.global.external;

import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2Service {

    private final WebClient webClient;
    private final KakaoProviderProperties kakaoProviderProperties;
    private final MemberService memberService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    // 토큰 재발급
    public String getAccessToken(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("refresh_token", refreshToken);

        Map<String, String> response = webClient.post()
                .uri(kakaoProviderProperties.getTokenUri())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        return response != null ? response.get("access_token") : null;
    }

    // 정보 추출
    public Authentication getAuthentication(String accessToken) {
        Map<String, Object> userInfoMap = webClient.get()
                .uri(kakaoProviderProperties.getUserInfoUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .blockOptional()
                .orElse(Collections.emptyMap());

        String loginId = "kakao_" + userInfoMap.get("id").toString();
        Member member = memberService.getMemberByLoginId(loginId).orElse(null);
        List<SimpleGrantedAuthority> authorities = (member != null)
                ? List.of(new SimpleGrantedAuthority(member.getRole().getKey()))
                : List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        User principal = new User(loginId, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    // 토큰 검증
    public boolean validateAccessToken(String accessToken) {
        return webClient.get()
                .uri(kakaoProviderProperties.getUserInfoUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .blockOptional()
                .orElse(false);
    }

    // 토큰 삭제
    public boolean revokeAccessToken(String accessToken) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName().substring(6);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", kakaoId);

        return webClient.post()
                .uri(kakaoProviderProperties.getTokenRevokeUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .blockOptional()
                .orElse(false);
    }

}
