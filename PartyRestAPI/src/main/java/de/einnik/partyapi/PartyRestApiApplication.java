package de.einnik.partyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Spring Application, the Main class of the Project
 * where every thing is handled by Spring Boot, which means
 * nothing needs to be registered here and everything is done
 * with Controllers and Services asynchronously
 */
@EnableAsync
@SpringBootApplication
public class PartyRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartyRestApiApplication.class, args);
    }
}