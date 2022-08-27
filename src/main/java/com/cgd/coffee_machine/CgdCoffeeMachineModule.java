package com.cgd.coffee_machine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@SpringBootApplication
public class CgdCoffeeMachineModule {

    public static final Logger LOGGER = LoggerFactory.getLogger(CgdCoffeeMachineModule.class);

    public static void main(String[] args) {
        SpringApplication.run(CgdCoffeeMachineModule.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
