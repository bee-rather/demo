package com.weather.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@EnableCaching
@ConfigurationPropertiesScan("com.weather.demo.config")
public class WeatherMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherMicroserviceApplication.class, args);
	}

}
