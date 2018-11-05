package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class ProfileEndpointConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(ProfileHandler handler) { // <1>
        return route(i(GET("/profiles")), handler::all) // <2>
            .andRoute(i(GET("/profiles/{id}")), handler::getById)
            .andRoute(i(DELETE("/profiles/{id}")), handler::deleteById) // <3>
            .andRoute(i(POST("/profiles")), handler::create)
            .andRoute(i(PUT("/profiles/{id}")), handler::updateById);
    }

    // <4>
    private static RequestPredicate i(RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }
}