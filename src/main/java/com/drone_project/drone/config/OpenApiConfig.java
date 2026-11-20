package com.drone_project.drone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI droneOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Drone Fleet API")
                        .description("REST API for managing drone fleet and medication deliveries")
                        .version("v1"));
    }
}
