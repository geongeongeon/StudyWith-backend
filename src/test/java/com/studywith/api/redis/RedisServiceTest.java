/*
package com.studywith.api.redis;

import com.studywith.api.global.redis.RedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    @DisplayName("레디스 데이터 저장, 조회, 삭제")
    void t001() {
        redisService.saveTokens(1L, "testAccessToken", "testRefreshToken");
        String accessToken = redisService.getAccessToken(1L);
        String refreshToken = redisService.getRefreshToken(1L);
        redisService.deleteTokens(1L);

        assertThat(accessToken).isEqualTo("testAccessToken");
        assertThat(refreshToken).isEqualTo("testRefreshToken");
        assertThat(redisService.getAccessToken(1L)).isNull();
        assertThat(redisService.getRefreshToken(1L)).isNull();
    }

}
*/
