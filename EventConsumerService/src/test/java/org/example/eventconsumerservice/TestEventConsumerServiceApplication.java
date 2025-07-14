package org.example.eventconsumerservice;

import org.springframework.boot.SpringApplication;

public class TestEventConsumerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(EventConsumerServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
