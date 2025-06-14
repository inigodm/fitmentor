package com.inigo.arch.user.infrastucture.spring;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // Enable CORS with our configuration and disable CSRF
        http.cors(cors -> cors.configurationSource(corsSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        // Set session management to stateless
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Set unauthorized requests exception handler
        http.exceptionHandling(handling ->
                handling.authenticationEntryPoint((request, response, ex) ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage())
                ));
        // Set permissions on endpoints
        http.authorizeHttpRequests(auth -> {
            auth
                .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                .requestMatchers(HttpMethod.PUT, "/user/create").permitAll()
                .requestMatchers( "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-ui*", "/v3/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/clients").permitAll()
                .requestMatchers("/**").authenticated();
                // The rest of them will be private
        });
        // Add JWT token filter
        http.addFilterAfter(
            authenticationFilter,
            UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Used by spring security if CORS is enabled.
    public CorsConfigurationSource corsSource() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS","PATCH"));
        config.setExposedHeaders(List.of("Authorization"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
