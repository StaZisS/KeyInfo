package org.example.key_info.transfer;

import org.example.key_info.core.client.repository.ClientRepositoryImpl;
import org.example.key_info.core.key.repository.KeyRepositoryImpl;
import org.example.key_info.core.transfer.TransferRepositoryImpl;
import org.example.key_info.core.transfer.TransferService;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.context.annotation.Configuration;

@Configuration
@JooqTest
@ImportAutoConfiguration({
        TransferService.class,
        ClientRepositoryImpl.class,
        KeyRepositoryImpl.class,
        TransferRepositoryImpl.class
})
public class TransferIntegrationTestConfiguration {

}
