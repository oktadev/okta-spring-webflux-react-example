package com.example.demo;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Log4j2
@WebFluxTest // <1>
public abstract class AbstractBaseProfileEndpoints {

    private final WebTestClient client; // <2>

    @MockBean  // <3>
    private ProfileRepository repository;

    public AbstractBaseProfileEndpoints(WebTestClient client) {
        this.client = client;
    }

    @Test
    public void getAll() {

        log.info("running  " + this.getClass().getName());

        // <4>
        Mockito
            .when(this.repository.findAll())
            .thenReturn(Flux.just(new Profile("1", "A"), new Profile("2", "B")));

        // <5>
        this.client
            .get()
            .uri("/profiles")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.[0].id").isEqualTo("1")
            .jsonPath("$.[0].email").isEqualTo("A")
            .jsonPath("$.[1].id").isEqualTo("2")
            .jsonPath("$.[1].email").isEqualTo("B");
    }

    @Test
    public void save() {
        Profile data = new Profile("123", UUID.randomUUID().toString() + "@email.com");
        Mockito
            .when(this.repository.save(Mockito.any(Profile.class)))
            .thenReturn(Mono.just(data));
        MediaType jsonUtf8 = MediaType.APPLICATION_JSON_UTF8;
        this
            .client
            .post()
            .uri("/profiles")
            .contentType(jsonUtf8)
            .body(Mono.just(data), Profile.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(jsonUtf8);
    }

    @Test
    public void delete() {
        Profile data = new Profile("123", UUID.randomUUID().toString() + "@email.com");
        Mockito
            .when(this.repository.findById(data.getId()))
            .thenReturn(Mono.just(data));
        Mockito
            .when(this.repository.deleteById(data.getId()))
            .thenReturn(Mono.empty());
        this
            .client
            .delete()
            .uri("/profiles/" + data.getId())
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    public void update() {
        Profile data = new Profile("123", UUID.randomUUID().toString() + "@email.com");

        Mockito
            .when(this.repository.findById(data.getId()))
            .thenReturn(Mono.just(data));

        Mockito
            .when(this.repository.save(data))
            .thenReturn(Mono.just(data));

        this
            .client
            .put()
            .uri("/profiles/" + data.getId())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(data), Profile.class)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    public void getById() {

        Profile data = new Profile("1", "A");

        Mockito
            .when(this.repository.findById(data.getId()))
            .thenReturn(Mono.just(data));

        this.client
            .get()
            .uri("/profiles/" + data.getId())
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.id").isEqualTo(data.getId())
            .jsonPath("$.email").isEqualTo(data.getEmail());
    }
}