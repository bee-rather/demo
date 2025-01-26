package com.weather.demo.service.impl;

//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.weather.demo.config.WeatherConfig;
import com.weather.demo.dto.WeatherRequestDto;
import com.weather.demo.dto.WeatherResponseDto;
import com.weather.demo.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    private final WebClient.Builder webClientBuilder;
    private final WeatherConfig weatherConfig;

    // Constructor for explicit dependency injection
    public WeatherServiceImpl(WebClient.Builder webClientBuilder, WeatherConfig weatherConfig) {
        this.webClientBuilder = webClientBuilder;
        this.weatherConfig = weatherConfig;
    }

    @Override
    @Cacheable(value = "weatherCache", key = "#request.city + #request.countryCode")
    // @CircuitBreaker(name = "weatherService", fallbackMethod = "fallbackWeather")
    public Mono<WeatherResponseDto> getWeatherForCity(WeatherRequestDto request) {
        String location = request.getCountryCode() != null
                ? request.getCity() + "," + request.getCountryCode()
                : request.getCity();
        System.err.println(weatherConfig.getFullApiUrl() + "&q=" + location);
        return webClientBuilder.build()
                .get()
                .uri(weatherConfig.getFullApiUrl() + "&q=" + location)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::mapToWeatherResponse);
    }

    public Mono<WeatherResponseDto> fallbackWeather(WeatherRequestDto request, Throwable ex) {
        log.error("Circuit breaker fallback for city: {}", request.getCity(), ex);
        WeatherResponseDto responseDto = new WeatherResponseDto();
        // Set location
        WeatherResponseDto.Location location = new WeatherResponseDto.Location();
        location.setName("Unavailable");
        responseDto.setLocation(location);
        return Mono.just(responseDto);
    }

    private WeatherResponseDto mapToWeatherResponse(String jsonResponse) {

        WeatherResponseDto responseDto = new WeatherResponseDto();
        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            // Set location
            WeatherResponseDto.Location location = new WeatherResponseDto.Location();
            location.setName(rootNode.path("name").asText());
            responseDto.setLocation(location);

            // Set forecast (you'll need to adapt this based on actual API response)
            List<WeatherResponseDto.ForecastEntry> forecast = new ArrayList<>();
            // Example parsing - adjust based on actual API structure
            JsonNode listNode = rootNode.path("list");
            for (JsonNode forecastNode : listNode) {
                WeatherResponseDto.ForecastEntry entry = new WeatherResponseDto.ForecastEntry();
                entry.setDt(forecastNode.path("dt").asLong());
                entry.setHighTemp(forecastNode.path("main").path("temp").asDouble());
                entry.setDescription(forecastNode.path("weather").get(0).path("description").asText());
                entry.setWindSpeed(forecastNode.path("wind").path("speed").asDouble());
                forecast.add(entry);
            }
            responseDto.setForecast(forecast);
            responseDto.setForecast(calculateDailyWeather(responseDto));
        } catch (JsonProcessingException e) {
        }

        return responseDto;
    }

    public List<WeatherResponseDto.ForecastEntry> calculateDailyWeather(WeatherResponseDto weatherResponse) {
        Map<LocalDate, List<WeatherResponseDto.ForecastEntry>> dailyWeatherData = new HashMap<>();


        for (WeatherResponseDto.ForecastEntry entry : weatherResponse.getForecast()) {
            LocalDate date = Instant.ofEpochSecond(entry.getDt()).atZone(ZoneId.systemDefault()).toLocalDate();
            dailyWeatherData.putIfAbsent(date, new ArrayList<>());
            dailyWeatherData.get(date).add(entry);
        }

        List<WeatherResponseDto.ForecastEntry> dailyWeather = new ArrayList<>();
        LocalDate today = LocalDate.now();
        dailyWeatherData.entrySet().stream()
                .filter(entry -> entry.getKey().isAfter(today))
                .sorted(Map.Entry.comparingByKey())
                .limit(3)
                .forEach(entry -> {
                    List<WeatherResponseDto.ForecastEntry> dailyData = entry.getValue();
                    double highTemp = dailyData.stream().mapToDouble(WeatherResponseDto.ForecastEntry::getHighTemp).max()
                            .orElse(0.0);
                    double lowTemp = dailyData.stream().mapToDouble(WeatherResponseDto.ForecastEntry::getHighTemp).min()
                            .orElse(0.0);
                    boolean rain = dailyData.stream()
                            .anyMatch(forecastEntry -> forecastEntry.getDescription().toLowerCase().contains("rain"));
                    boolean thunderstorm = dailyData.stream().anyMatch(forecastEntry -> forecastEntry.getDescription().toLowerCase().contains("thunderstorm"));
                    boolean highWind = dailyData.stream().anyMatch(forecastEntry -> forecastEntry.getWindSpeed() > 10);

                    String advice = "";
                    if (thunderstorm) {
                        advice = "Don’t step out! A Storm is brewing!";
                    } else if (rain) {
                        advice = "Carry umbrella";
                    } else if (highWind) {
                        advice = "It’s too windy, watch out!";
                    } else if (highTemp > 40.0) {
                        advice = "Use sunscreen lotion";
                    }

                    WeatherResponseDto.ForecastEntry entryData = new WeatherResponseDto.ForecastEntry();
                    entryData.setDt(entry.getKey().atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
                    entryData.setLowTemp(lowTemp);
                    entryData.setHighTemp(highTemp);  // Example setting high temperature
                    entryData.setDescription(advice);
                    entryData.setWindSpeed(highTemp);  // Set high windspeed for the day

                    dailyWeather.add(entryData);
                });

        return dailyWeather;
    }
}