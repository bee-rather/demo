package com.weather.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "openweathermap")
@Getter
@Setter
public class WeatherConfig {

    @NotNull
    @NotEmpty
    private String apiKey;

    @NotNull
    @NotEmpty
    private String baseUrl;

    // Optional: Add validation or custom logic
    public String getFullApiUrl() {
        return baseUrl + "?appid=" + apiKey + "&cnt=72";
    }
}