package com.oceans7.dib.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String title = "DIB Swagger";
        String description = "DIB Rest API Docs Page";
        String version = "1.0.0";

        Info info = new Info().title(title).description(description).version(version);

        return new OpenAPI().info(info).components(
                new Components().addSecuritySchemes(
                        "bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                )
        );
    }
}
