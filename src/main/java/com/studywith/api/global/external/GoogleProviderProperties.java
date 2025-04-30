package com.studywith.api.global.external;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.provider.google")
@Getter
@Setter
public class GoogleProviderProperties {

    private String userInfoUri;
    private String tokenUri;
    private String tokenInfoUri;
    private String tokenRevokeUri;

}
