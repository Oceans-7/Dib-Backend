package com.oceans7.dib.global.api.config;

import com.oceans7.dib.global.api.http.DataGoKrApi;
import com.oceans7.dib.global.api.http.KakaoApi;
import com.oceans7.dib.global.api.http.KakaoAuthApi;
import com.oceans7.dib.global.api.http.KhoaGoKrApi;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class OpenApiConfig {

    @Value("${open-api.data-go-kr.base-url}")
    private String dataGoKrBaseUrl;

    @Value("${open-api.khoa-go-kr.base-url}")
    private String KhoaGoKrBaseUrl;

    @Value("${open-api.kakao.base-url}")
    private String kakaoBaseUrl;

    @Value("${open-api.kakao.service-key}")
    private String kakaoServiceKey;

    @Value("${open-api.kakao.open-key-url}")
    private String kakaoOpenKeyUrl;


    private final static String kakaoHeader = "KakaoAK ";

    private ConnectionProvider myConnectionProvider() {
        return ConnectionProvider.builder("myConnectionProvider")
                .maxConnections(50) // 최대 연결 수
                .maxIdleTime(Duration.ofSeconds(60)) // 연결 유지 시간
                .build();
    }

    private HttpClient httpClient() {
        return HttpClient.create(myConnectionProvider())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
                .responseTimeout(Duration.ofMillis(50000))
                .doOnConnected(conn ->
                                conn.addHandlerLast(new ReadTimeoutHandler(50000, TimeUnit.MILLISECONDS))
                                    .addHandlerLast(new WriteTimeoutHandler(50000, TimeUnit.MILLISECONDS))
                ).resolver(DefaultAddressResolverGroup.INSTANCE);
    }

    @Bean
    DataGoKrApi dataGoKrApi() {

        WebClient webClient = WebClient.builder()
                .baseUrl(dataGoKrBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .blockTimeout(Duration.ofMillis(30000))
                .build()
                .createClient(DataGoKrApi.class);
    }

    @Bean
    KhoaGoKrApi khoaGoKrApi() {

        WebClient webClient = WebClient.builder()
                .baseUrl(KhoaGoKrBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .blockTimeout(Duration.ofMillis(50000))
                .build()
                .createClient(KhoaGoKrApi.class);
    }


    @Bean
    KakaoApi kakaoApi() {

        WebClient webClient = WebClient.builder()
                .baseUrl(kakaoBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, kakaoHeader + kakaoServiceKey)
                .build();

        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .blockTimeout(Duration.ofMillis(5000))
                .build()
                .createClient(KakaoApi.class);
    }

    @Bean
    KakaoAuthApi kakaoAuthApi() {

        WebClient webClient = WebClient.builder()
                .baseUrl(kakaoOpenKeyUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .blockTimeout(Duration.ofMillis(5000))
                .build()
                .createClient(KakaoAuthApi.class);
    }

}
