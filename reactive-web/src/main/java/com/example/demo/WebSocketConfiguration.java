package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Log4j2
@Configuration
class WebSocketConfiguration {

    // <1>
    @Bean
    Executor executor() {
        return Executors.newSingleThreadExecutor();
    }

    // <2>
    @Bean
    HandlerMapping handlerMapping(WebSocketHandler wsh) {
        return new SimpleUrlHandlerMapping() {
            {
                // <3>
                setUrlMap(Collections.singletonMap("/ws/profiles", wsh));
                setOrder(10);
            }
        };
    }

    // <4>
    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    WebSocketHandler webSocketHandler(
        ObjectMapper objectMapper, // <5>
        ProfileCreatedEventPublisher eventPublisher // <6>
    ) {

        Flux<ProfileCreatedEvent> publish = Flux
            .create(eventPublisher)
            .share(); // <7>

        return session -> {

            Flux<WebSocketMessage> messageFlux = publish
                .map(evt -> {
                    try {
                        // <8>
                        Profile profile = (Profile) evt.getSource();
                        Map<String, String> data = new HashMap<>();
                        data.put("id", profile.getId());
                        return objectMapper.writeValueAsString(data);
                    }
                    catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(str -> {
                    log.info("sending " + str);
                    return session.textMessage(str);
                });

            return session.send(messageFlux); // <9>
        };
    }

}