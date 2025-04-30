package com.studywith.api.global.external;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.provider.naver")
@Getter
@Setter
public class NaverProviderProperties {

    private String userInfoUri;
    private String tokenUri;

}
