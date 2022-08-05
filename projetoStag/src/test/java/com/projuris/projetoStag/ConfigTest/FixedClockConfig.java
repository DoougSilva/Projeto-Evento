package com.projuris.projetoStag.ConfigTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@TestConfiguration
public
class FixedClockConfig {

    @Primary
    @Bean
    Clock fixedClock() {
        return Clock.fixed(
                Instant.parse("2022-07-28T08:00:00.000Z"),
                ZoneId.of("GMT"));
    }
}


