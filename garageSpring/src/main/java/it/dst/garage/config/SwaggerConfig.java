package it.dst.garage.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        @Bean
        OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .addSecurityItem(new SecurityRequirement()
                                                .addList("Bearer Authentication")
                                                .addList("Basic Authentication"))
                                .components(new Components()
                                                .addSecuritySchemes("Bearer Authentication",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT"))
                                                .addSecuritySchemes("Basic Authentication",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("basic")))
                                .info(new Info()
                                                .title("Garage API")
                                                .version("1.0.0")
                                                .description("API to manage cars in the garage")
                                                .contact(new Contact()
                                                                .name("JonayKB")
                                                                .email("jonaykb@gmail.com"))
                                                .license(new License()
                                                                .name("MIT License")
                                                                .url("http://springdoc.org")));
        }

}