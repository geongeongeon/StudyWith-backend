package com.studywith.api.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisRepository redisRepository;

    private static final String ACCESS_TOKENS_PREFIX = "access-tokens:";
    private static final String REFRESH_TOKENS_PREFIX = "refresh-tokens:";

    public static final long ACCESS_TOKEN_EXPIRATION = 60; // 1분
    public static final long REFRESH_TOKEN_EXPIRATION = 60 * 60 * 24; // 1일

    public void saveTokens(Long id, String accessToken, String refreshToken) {
        String accessTokenKey = ACCESS_TOKENS_PREFIX + id;
        redisRepository.save(accessTokenKey, accessToken, ACCESS_TOKEN_EXPIRATION);

        String refreshTokenKey = REFRESH_TOKENS_PREFIX + id;
        redisRepository.save(refreshTokenKey, refreshToken, REFRESH_TOKEN_EXPIRATION);
    }

    public String getAccessToken(Long id) {
        String key = ACCESS_TOKENS_PREFIX + id;

        return redisRepository.get(key);
    }

    public String getRefreshToken(Long id) {
        String key = REFRESH_TOKENS_PREFIX + id;

        return redisRepository.get(key);
    }

    public void deleteTokens(Long id) {
        String accessTokenKey = ACCESS_TOKENS_PREFIX + id;
        redisRepository.delete(accessTokenKey);

        String refreshTokenKey = REFRESH_TOKENS_PREFIX + id;
        redisRepository.delete(refreshTokenKey);
    }

}
