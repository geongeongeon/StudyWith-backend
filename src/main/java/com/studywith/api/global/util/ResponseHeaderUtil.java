package com.studywith.api.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.util.Arrays;

public class ResponseHeaderUtil {

    public static String getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public static String getAuthorization(HttpServletRequest request, String prefix) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(prefix)) {
            return header.substring(prefix.length());
        }

        return null;
    }

    public static void setCookie(HttpServletResponse response, String name, String value, Long age) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofSeconds(age))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static void setAuthorization(HttpServletResponse response, String value) {
        response.addHeader(HttpHeaders.AUTHORIZATION, value);
    }

}
