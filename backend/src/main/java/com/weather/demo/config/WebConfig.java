package com.weather.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.EnableWebFlux;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import java.util.Collections;

@Configuration
@EnableWebFlux
@OpenAPIDefinition(
    info = @Info(
        title = "Weather Microservice", 
        version = "1.0", 
        description = "Reactive Weather Information Service"
    )
)
public class WebConfig {

    // @Bean
    // public CorsFilter corsFilter() {
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     CorsConfiguration config = new CorsConfiguration();
    //     config.setAllowCredentials(true);
    //     config.addAllowedOrigin("*");
    //     config.addAllowedHeader("*");
    //     config.addAllowedMethod("*");
    //     source.registerCorsConfiguration("/**", config);
    //     return new CorsFilter(source);
    // }

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http.cors().disable();  // Disable CORS
    //     http.csrf().disable();  // Disable CSRF
    //     http.authorizeRequests()
    //         .anyRequest().permitAll();
    //     return http.build();
    // }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for Postman testing
            .cors(cors -> cors.disable())
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/api/**","/swagger-ui.html")
                .permitAll()
                .anyExchange().authenticated()
            ).httpBasic(); // Optional: enable HTTP Basic auth if needed

        return http.build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("*"));
        corsConfig.setMaxAge(3600L);
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}