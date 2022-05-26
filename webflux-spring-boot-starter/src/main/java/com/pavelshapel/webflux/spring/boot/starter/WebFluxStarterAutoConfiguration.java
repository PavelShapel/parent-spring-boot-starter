package com.pavelshapel.webflux.spring.boot.starter;

import com.pavelshapel.webflux.spring.boot.starter.annotation.ConditionalOnPropertyWebClient;
import com.pavelshapel.webflux.spring.boot.starter.properties.WebClientProperties;
import com.pavelshapel.webflux.spring.boot.starter.properties.WebFluxProperties;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static io.netty.handler.logging.LogLevel.DEBUG;
import static java.time.Duration.ofMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static reactor.netty.transport.logging.AdvancedByteBufFormat.TEXTUAL;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebFluxStarterAutoConfiguration {
    public static final String TYPE = "webflux";
    public static final int DEFAULT_TIMEOUT = 1000;

    @Bean
    public WebFluxContextRefreshedListener webFluxContextRefreshedListener() {
        return new WebFluxContextRefreshedListener();
    }

    @Bean
    public WebFluxProperties webFluxProperties() {
        return new WebFluxProperties();
    }

    @Bean
    @ConditionalOnPropertyWebClient
    public WebClient webClient(WebFluxProperties webFluxProperties) {
        int timeout = getTimeout(webFluxProperties);
        String baseUrl = getBaseUrl(webFluxProperties);
        HttpClient httpClient = createHttpClient(timeout);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private HttpClient createHttpClient(int timeout) {
        SslContext sslContext = createSslContext();
        return HttpClient
                .create()
                .wiretap("reactor.netty.http.client.HttpClient", DEBUG, TEXTUAL)
                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext))
                .option(CONNECT_TIMEOUT_MILLIS, timeout)
                .responseTimeout(ofMillis(timeout))
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, MILLISECONDS));
                });
    }

    @SneakyThrows
    private SslContext createSslContext() {
        return SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
    }

    private int getTimeout(WebFluxProperties webFluxProperties) {
        return Optional.ofNullable(webFluxProperties)
                .map(WebFluxProperties::getWebClient)
                .map(WebClientProperties::getTimeout)
                .filter(timeout -> timeout >= 0)
                .orElse(DEFAULT_TIMEOUT);
    }

    private String getBaseUrl(WebFluxProperties webFluxProperties) {
        return Optional.ofNullable(webFluxProperties)
                .map(WebFluxProperties::getWebClient)
                .map(WebClientProperties::getBaseUrl)
                .filter(StringUtils::hasLength)
                .orElse(EMPTY);
    }
}