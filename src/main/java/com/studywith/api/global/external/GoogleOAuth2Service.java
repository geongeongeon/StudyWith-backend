package com.studywith.api.global.external;

import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.member.exception.MemberNotFoundException;
import com.studywith.api.domain.member.service.MemberService;
import com.studywith.api.global.common.GoogleProviderProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleOAuth2Service {

    private final WebClient webClient;
    private final GoogleProviderProperties googleProvider;
    private final MemberService memberService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    // 토큰 재발급
    public String getAccessToken(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);

        Map<String, String> response = webClient.post()
                .uri(googleProvider.getTokenUri())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        return response != null ? response.get("access_token") : null;
    }

    // 정보 추출
    public Authentication getAuthentication(String accessToken) {
        Mono<Map<String, Object>> userInfoMono = webClient.get()
                .uri(googleProvider.getUserInfoUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, Object> userInfoMap = userInfoMono.block();
        if (userInfoMap == null) {
            throw new RuntimeException("구글 사용자 정보 요청이 실패했습니다.");
        }

        String loginId = "google_" + userInfoMap.get("sub");
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
                .uri(uriBuilder -> uriBuilder.path(googleProvider.getTokenInfoUri()).queryParam("access_token", accessToken).build())
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(new RuntimeException("액세스 토큰 검증이 실패했습니다.")))
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .blockOptional()
                .orElse(false);
    }

    // 토큰 삭제
    public boolean revokeAccessToken(String accessToken) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(googleProvider.getTokenRevokeUri())
                        .queryParam("token", accessToken)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(new RuntimeException("토큰 삭제가 실패했습니다.")))
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .blockOptional()
                .orElse(false);
    }

}
