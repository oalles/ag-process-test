package com.example.agprocesstest;

import com.example.agprocesstest.events.Event;
import com.example.agprocesstest.events.EventRepository;
import com.example.agprocesstest.sources.SourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootApplication
@Slf4j
public class AgProcessTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgProcessTestApplication.class, args);
    }
}
