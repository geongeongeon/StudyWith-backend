package com.studywith.api.global.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final String key;
    private final Map<String, Object> attributes;
    private final String loginId;
    private final String role;
    private final String uuid;

    @Override
    public String getName() {
        return attributes.get(key).toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (role == null)
                ? List.of((new SimpleGrantedAuthority("ROLE_ANONYMOUS")))
                : List.of(new SimpleGrantedAuthority(role));
    }

}
