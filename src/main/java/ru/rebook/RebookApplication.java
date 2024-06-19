package ru.rebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RebookApplication {
    public static void main(String[] args) {
        SpringApplication.run(RebookApplication.class, args);
    }
}
