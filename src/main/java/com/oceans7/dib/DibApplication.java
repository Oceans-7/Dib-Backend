package com.oceans7.dib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAsync
@EnableWebMvc
@SpringBootApplication
public class DibApplication {

    public static void main(String[] args) {
        SpringApplication.run(DibApplication.class, args);
    }

}
