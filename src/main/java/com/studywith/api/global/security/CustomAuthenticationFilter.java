package com.studywith.api.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studywith.api.global.util.ResponseHeaderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends GenericFilterBean {

    private final OAuth2TokenProvider oAuth2TokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String registrationId = ResponseHeaderUtil.getCookie(request, "REGISTRATION_ID");
        if (registrationId == null) {
            setError(response, "인증되지 않은 사용자입니다.");
            return;
        }

        String accessToken = ResponseHeaderUtil.getAuthorization(request, "Bearer ");
        String refreshToken = ResponseHeaderUtil.getCookie(request, "REFRESH_TOKEN");

        if (accessToken != null && oAuth2TokenProvider.validateAccessToken(registrationId, accessToken)) {
            Authentication authentication = oAuth2TokenProvider.getAuthentication(registrationId, accessToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (refreshToken != null) {
            String newAccessToken = oAuth2TokenProvider.getAccessToken(registrationId, refreshToken);
            Authentication authentication = oAuth2TokenProvider.getAuthentication(registrationId, newAccessToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(servletRequest, servletResponse);
    }

    private void setError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), Map.of("message", message));
    }

}
