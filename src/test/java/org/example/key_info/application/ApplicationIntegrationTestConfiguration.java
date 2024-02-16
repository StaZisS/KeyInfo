package org.example.key_info.application;

import org.example.key_info.core.application.ApplicationRepositoryImpl;
import org.example.key_info.core.application.ApplicationService;
import org.example.key_info.core.client.repository.ClientRepositoryImpl;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.context.annotation.Configuration;

@Configuration
@JooqTest
@ImportAutoConfiguration({
        ApplicationService.class,
        ApplicationRepositoryImpl.class,
        ClientRepositoryImpl.class
})
public class ApplicationIntegrationTestConfiguration {
}
