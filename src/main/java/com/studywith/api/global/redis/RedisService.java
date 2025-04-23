package com.studywith.api.global.redis;

import com.studywith.api.global.external.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.studywith.api.global.common.Constants.*;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisRepository redisRepository;

    private static final String USER_INFO_PREFIX = "auth:temp:";
    private static final String ACCESS_TOKEN_PREFIX = "auth:access-token:";
    private static final String REFRESH_TOKEN_PREFIX = "auth:refresh-token:";

    public void saveUserInfo(String uuid, OAuth2UserInfo info) {
        String key = USER_INFO_PREFIX + uuid;
        redisRepository.save(key, info, OAUTH2_EXPIRATION);
    }

    public void saveTokens(String loginId, String accessToken, String refreshToken) {
        redisRepository.save(ACCESS_TOKEN_PREFIX + loginId, accessToken, ACCESS_TOKEN_EXPIRATION);
        redisRepository.save(REFRESH_TOKEN_PREFIX + loginId, refreshToken, REFRESH_TOKEN_EXPIRATION);
    }

    @SuppressWarnings("unchecked")
    public OAuth2UserInfo getUserInfo(String uuid) {
        Map<String, String> infoMap = redisRepository.get(USER_INFO_PREFIX + uuid, HashMap.class).orElse(null);

        return infoMap == null ? null : new OAuth2UserInfo(infoMap.get("loginId"), infoMap.get("email"), infoMap.get("accountType"));
    }

    public String getAccessToken(String loginId) {
        return redisRepository.get(ACCESS_TOKEN_PREFIX + loginId, String.class).orElse(null);
    }

    public String getRefreshToken(String loginId) {
        return redisRepository.get(REFRESH_TOKEN_PREFIX + loginId, String.class).orElse(null);
    }

    public void deleteUserInfo(String uuid) {
        redisRepository.delete(USER_INFO_PREFIX + uuid);
    }

    public void deleteTokens(String loginId) {
        redisRepository.delete(ACCESS_TOKEN_PREFIX + loginId);
        redisRepository.delete(REFRESH_TOKEN_PREFIX + loginId);
    }

}
