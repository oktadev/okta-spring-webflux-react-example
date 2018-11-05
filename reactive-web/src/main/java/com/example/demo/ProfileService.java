package com.example.demo;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
class ProfileService {

    private final ApplicationEventPublisher publisher; // <1>
    private final ProfileRepository profileRepository; // <2>

    ProfileService(ApplicationEventPublisher publisher, ProfileRepository profileRepository) {
        this.publisher = publisher;
        this.profileRepository = profileRepository;
    }

    public Flux<Profile> all() { // <3>
        return this.profileRepository.findAll();
    }

    public Mono<Profile> get(String id) { // <4>
        return this.profileRepository.findById(id);
    }

    public Mono<Profile> update(String id, String email) { // <5>
        return this.profileRepository
            .findById(id)
            .map(p -> new Profile(p.getId(), email))
            .flatMap(this.profileRepository::save);
    }

    public Mono<Profile> delete(String id) { // <6>
        return this.profileRepository
            .findById(id)
            .flatMap(p -> this.profileRepository.deleteById(p.getId()).thenReturn(p));
    }

    public Mono<Profile> create(String email) { // <7>
        return this.profileRepository
            .save(new Profile(null, email))
            .doOnSuccess(profile -> this.publisher.publishEvent(new ProfileCreatedEvent(profile)));
    }
}