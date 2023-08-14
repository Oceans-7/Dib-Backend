package com.oceans7.dib.global.config;

import com.oceans7.dib.domain.auth.user_account.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@RequiredArgsConstructor
@Configuration
public class AuthenticationManagerConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    /*
     * AuthenticationManager를 주입받기 위해서 빈으로 등록한다.
     * */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return new ProviderManager(jwtAuthenticationProvider);
    }
}

