package com.weather.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Weather request details")
public class WeatherRequestDto {
    @NotBlank(message = "City is required")
    @Size(min = 2, max = 100, message = "City name must be between 2 and 100 characters")
    @Schema(description = "City name", example = "London")
    private String city;

    @Size(min = 2, max = 3, message = "Country code must be 2-3 characters")
    @Schema(description = "Two-letter country code", example = "GB")
    private String countryCode;
}