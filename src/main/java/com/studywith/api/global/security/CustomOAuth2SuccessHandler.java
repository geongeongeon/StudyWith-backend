package com.studywith.api.global.security;

import com.studywith.api.global.redis.RedisService;
import com.studywith.api.global.util.ResponseHeaderUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

import static com.studywith.api.global.common.Constants.OAUTH2_EXPIRATION;
import static com.studywith.api.global.common.Constants.REFRESH_TOKEN_EXPIRATION;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final RedisService redisService;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        String principalName = authentication.getName();
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient(registrationId, principalName);
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        String refreshToken = oAuth2AuthorizedClient.getRefreshToken().getTokenValue();

        redisService.setAccessToken(user.getLoginId(), accessToken);
        redisService.setRefreshToken(user.getLoginId(), refreshToken);
        redisService.setLoginId(user.getLoginId(), refreshToken);

        ResponseHeaderUtil.setAuthorization(response, "Bearer " + accessToken);
        ResponseHeaderUtil.setCookie(response, "REFRESH_TOKEN", refreshToken, REFRESH_TOKEN_EXPIRATION);
        ResponseHeaderUtil.setCookie(response, "REGISTRATION_ID", registrationId, REFRESH_TOKEN_EXPIRATION);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        boolean isAnonymous = authorities.stream().anyMatch(auth -> "ROLE_ANONYMOUS".equals(auth.getAuthority()));
        if (isAnonymous) {
            String uuid = user.getUuid();
            ResponseHeaderUtil.setCookie(response, "UUID", uuid, OAUTH2_EXPIRATION);
            response.sendRedirect("http://localhost:5173/signup/step1");

            return;
        }

        response.sendRedirect("http://localhost:5173/?status=200");
    }

}
