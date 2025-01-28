package com.weather.demo.service.Impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import com.weather.demo.config.WeatherConfig;
import com.weather.demo.dto.WeatherRequestDto;
import com.weather.demo.dto.WeatherResponseDto;
import com.weather.demo.service.impl.WeatherServiceImpl;
import com.weather.demo.service.impl.WeatherServiceImpl.WeatherNotFoundException;
import com.weather.demo.service.impl.WeatherServiceImpl.WeatherParsingException;
import com.weather.demo.service.impl.WeatherServiceImpl.WeatherServiceException;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> uriSpec;
    
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private WeatherConfig weatherConfig;

    @InjectMocks
    private WeatherServiceImpl weatherServiceImpl;

    @BeforeEach
    public void setUp() {
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenAnswer(invocation -> {
            // Using Answer interface to handle the return type
            return requestHeadersUriSpec;
        });
        when(requestHeadersUriSpec.uri(any(String.class))).thenAnswer(invocation -> {
            // Using Answer interface to handle the return type
            return requestHeadersUriSpec;
        });
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void testGetWeatherForCity_Success() {
        WeatherRequestDto request = new WeatherRequestDto();
        request.setCity("New York");
        request.setCountryCode("US");

        when(weatherConfig.getFullApiUrl()).thenReturn("http://example.com/api");
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("{\"name\":\"New York\",\"list\":[]}"));

        Mono<WeatherResponseDto> responseMono = weatherServiceImpl.getWeatherForCity(request);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals("New York", response.getLocation().getName());
                })
                .verifyComplete();
    }

    @Test
    public void testGetWeatherForCity_NotFound() {
        WeatherRequestDto request = new WeatherRequestDto();
        request.setCity("UnknownCity");

        when(weatherConfig.getFullApiUrl()).thenReturn("http://example.com/api");
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.error(WebClientResponseException.create(404, "Not Found", null, null, null)));

        Mono<WeatherResponseDto> responseMono = weatherServiceImpl.getWeatherForCity(request);

        StepVerifier.create(responseMono)
                .expectError(WeatherNotFoundException.class)
                .verify();
    }

    @Test
    public void testGetWeatherForCity_JsonProcessingException() {
        WeatherRequestDto request = new WeatherRequestDto();
        request.setCity("New York");

        when(weatherConfig.getFullApiUrl()).thenReturn("http://example.com/api");
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("invalid json"));

        Mono<WeatherResponseDto> responseMono = weatherServiceImpl.getWeatherForCity(request);

        StepVerifier.create(responseMono)
                .expectError(WeatherParsingException.class)
                .verify();
    }

    @Test
    public void testGetWeatherForCity_WebClientException() {
        WeatherRequestDto request = new WeatherRequestDto();
        request.setCity("New York");

        when(weatherConfig.getFullApiUrl()).thenReturn("http://example.com/api");
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new Error("Error")));

        Mono<WeatherResponseDto> responseMono = weatherServiceImpl.getWeatherForCity(request);

        StepVerifier.create(responseMono)
                .expectError(WeatherServiceException.class)
                .verify();
    }

    @Test
    public void testFallbackWeather() {
        WeatherRequestDto request = new WeatherRequestDto();
        request.setCity("New York");

        Mono<WeatherResponseDto> fallbackMono = weatherServiceImpl.fallbackWeather(request, new RuntimeException("Test"));

        StepVerifier.create(fallbackMono)
                .assertNext(response -> {
                    assertEquals("Unavailable", response.getLocation().getName());
                })
                .verifyComplete();
    }
}
