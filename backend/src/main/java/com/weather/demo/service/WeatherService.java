package com.weather.demo.service;

import com.weather.demo.dto.WeatherRequestDto;
import com.weather.demo.dto.WeatherResponseDto;

import reactor.core.publisher.Mono;

public interface WeatherService {
    Mono<WeatherResponseDto> getWeatherForCity(WeatherRequestDto request);
}