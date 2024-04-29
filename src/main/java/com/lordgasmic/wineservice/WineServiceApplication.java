package com.lordgasmic.wineservice;

import com.lordgasmic.collections.Nucleus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class WineServiceApplication {

    public static void main(final String... args) {
        Nucleus.start();
        SpringApplication.run(WineServiceApplication.class, args);
    }
}
