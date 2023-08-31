package com.oceans7.dib.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String title = "DIB Swagger";
        String description = "DIB Rest API Docs Page";
        String version = "1.0.0";

        Info info = new Info().title(title).description(description).version(version);

        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("Authorization");

        return new OpenAPI().info(info)
                        .addSecurityItem(addSecurityItem)
                        .components(
                                new Components().addSecuritySchemes(
                                        "Authorization",
                                        getJwtSecurityScheme()
                                )
                        );
    }

    private SecurityScheme getJwtSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Authorization")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);
    }

}
