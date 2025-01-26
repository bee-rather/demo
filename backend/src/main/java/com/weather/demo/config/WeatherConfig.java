package com.weather.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "openweathermap")
@Getter
@Setter
public class WeatherConfig {
    private String apiKey;
    private String baseUrl;

    // Optional: Add validation or custom logic
    public String getFullApiUrl() {
        return baseUrl + "?appid=" + apiKey + "&cnt=72";
    }
}