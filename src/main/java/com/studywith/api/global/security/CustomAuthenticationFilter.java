package com.studywith.api.global.security;

import com.studywith.api.global.redis.RedisService;
import com.studywith.api.global.util.ResponseHeaderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends GenericFilterBean {

    private final OAuth2TokenProvider oAuth2TokenProvider;
    private final RedisService redisService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String registrationId = ResponseHeaderUtil.getCookie(request, "REGISTRATION_ID");
        String accessToken = ResponseHeaderUtil.getAuthorization(request, "Bearer ");
        String refreshToken = ResponseHeaderUtil.getCookie(request, "REFRESH_TOKEN");

        if (accessToken != null && oAuth2TokenProvider.validateAccessToken(registrationId, accessToken)) {
            Authentication authentication = oAuth2TokenProvider.getAuthentication(registrationId, accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (refreshToken != null) {
            String newAccessToken = oAuth2TokenProvider.getAccessToken(registrationId, refreshToken);
            Authentication authentication = oAuth2TokenProvider.getAuthentication(registrationId, newAccessToken);
            redisService.setAccessToken(authentication.getName(), newAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(servletRequest, servletResponse);
    }

}
