package com.oceans7.dib.global.api.config;

import com.oceans7.dib.global.api.http.DataGoKrApi;
import com.oceans7.dib.global.api.http.KakaoApi;
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

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class OpenApiConfig {

    @Value("${open-api.data-go-kr.base-url}")
    private String dataGoKrBaseUrl;

    @Value("${open-api.kakao.base-url}")
    private String kakaoBaseUrl;

    @Value("${open-api.kakao.service-key}")
    private String kakaoServiceKey;

    private final static String kakaoHeader = "KakaoAK ";

    private HttpClient httpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                                conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                    .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
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

}
