package org.example.key_info.application;

import jakarta.annotation.PostConstruct;
import org.example.key_info.core.application.ApplicationRepositoryImpl;
import org.example.key_info.core.application.ApplicationService;
import org.example.key_info.core.client.repository.ClientRepositoryImpl;
import org.example.key_info.core.timeslot.TimeSlotRepositoryImpl;
import org.example.key_info.core.timeslot.TimeSlotService;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
@JooqTest
@ImportAutoConfiguration({
        ApplicationService.class,
        ApplicationRepositoryImpl.class,
        ClientRepositoryImpl.class,
        TimeSlotService.class,
        TimeSlotRepositoryImpl.class
})
public class ApplicationIntegrationTestConfiguration {
    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
