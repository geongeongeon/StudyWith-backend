package com.studywith.api.global.security;

import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.member.service.MemberService;
import com.studywith.api.global.external.OAuth2UserInfo;
import com.studywith.api.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final RedisService redisService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        String registrationId = request.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = super.loadUser(request).getAttributes();
        OAuth2UserInfo info = OAuth2UserInfo.from(registrationId, attributes);

        Member member = memberService.getMemberByLoginId(info.getLoginId()).orElse(null);
        String role = (member == null) ?  null : member.getRole().getKey();
        String uuid = (member == null) ? UUID.randomUUID().toString() : null;
        if (member == null) {
            redisService.saveUserInfo(uuid, info);
        }

        String key = request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        return new CustomOAuth2User(key, attributes, info.getLoginId(), role, uuid);
    }

}
