package com.sistema.flashcards.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Flashcards Inteligentes")
                        .version("1.0.0")
                        .description("Documentación de la API de la plataforma de Flashcards Inteligentes utilizando el sistema Leitner.")
                        .contact(new Contact()
                                .name("Soporte Synap-B")
                                .email("soporte@synapb.com")));
    }
}
