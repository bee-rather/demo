package com.weather.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Weather Forecast Data")
public class WeatherResponseDto {
    @Schema(description = "Location details")
    private Location location;

    @Schema(description = "Forecast data for multiple time periods")
    private List<ForecastEntry> forecast;

    @Data
    @NoArgsConstructor
    @Schema(description = "Location information")
    public static class Location {
        @Schema(description = "Name of the city", example = "London")
        private String name;
    }

    @Data
    @NoArgsConstructor
    @Schema(description = "Forecast entry for a specific time period")
    public static class ForecastEntry {
        @Schema(description = "Timestamp", example = "1737666000")
        private Long dt;

        @Schema(description = "Temperature in Kelvin", example = "278.47")
        private Double lowTemp;

        @Schema(description = "Temperature in Kelvin", example = "278.47")
        private Double highTemp;

        @Schema(description = "Weather description", example = "broken clouds & Thunderstorm")
        private String description;

        @Schema(description = "Wind speed", example = "4.54")
        private Double windSpeed;
    }
}