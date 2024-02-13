package org.example.key_info.auth;

import org.example.key_info.core.auth.config.RedisConfig;
import org.example.key_info.core.auth.provider.JwtProvider;
import org.example.key_info.core.auth.repository.RefreshRepositoryImpl;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration({
        JwtProvider.class,
        RedisConfig.class,
        RefreshRepositoryImpl.class
})
public class AuthIntegrationTestConfiguration {
}
