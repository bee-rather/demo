package com.weather.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.weather.demo.dto.WeatherRequestDto;
import com.weather.demo.dto.WeatherResponseDto;
import com.weather.demo.service.WeatherService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Tag(name = "Weather API", description = "Weather information retrieval")
public class WeatherController {
    private final WeatherService weatherService;

    @PostMapping(value = "/forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get current weather", description = "Retrieve current weather for a city")
    public Mono<WeatherResponseDto> getCurrentWeather(@Valid @RequestBody WeatherRequestDto request) {
        
        return weatherService.getWeatherForCity(request);
    }

    @PostMapping(value = "/test")
    public String getTestValue() {
        return "successfull";
    }
}