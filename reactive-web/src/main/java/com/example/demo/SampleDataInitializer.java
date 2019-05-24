package com.example.demo;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Log4j2 // <1>
@Component
@org.springframework.context.annotation.Profile("demo") // <2>
class SampleDataInitializer
    implements ApplicationListener<ApplicationReadyEvent> {

    private final ProfileRepository repository; // <3>

    public SampleDataInitializer(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        repository
            .deleteAll() // <4>
            .thenMany(
                Flux
                    .just("A", "B", "C", "D") // <5>
                    .map(name -> new Profile(UUID.randomUUID().toString(), name + "@email.com")) // <6>
                    .flatMap(repository::save) // <7>
            )
            .thenMany(repository.findAll()) // <8>
            .subscribe(profile -> log.info("saving " + profile.toString())); // <9>
    }
}