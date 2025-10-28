package com.hotelmanagement.scru167.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;

@Configuration
public class JacksonConfig {
    @Bean
    ObjectMapper objectMapper() {
        var m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        return m;
    }
}

