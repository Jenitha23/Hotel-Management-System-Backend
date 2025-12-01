package com.palmbeachresort.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionAuthenticationFilter sessionAuthenticationFilter() {
        return new SessionAuthenticationFilter();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Add both frontend ports
        configuration.setAllowedOrigins(List.of("https://frontend-palmbeachresort.vercel.app", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type")); // Important for CORS

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Public endpoints
                        .requestMatchers(
                                "/api/customers/auth/register",
                                "/api/customers/auth/login",
                                "/api/staff/auth/login",
                                "/api/admin/auth/login",
                                "/api/auth/login",
                                "/api/auth/logout",
                                "/api/customer/rooms/**",
                                "/api/customer/rooms/types/**",
                                "/api/customer/menu/**",
                                "/api/customer/cart/**",
                                "/api/customer/orders/**",
                                "/error"
                        ).permitAll()

                        // Staff/Admin login endpoints - Public but hidden in UI
                        .requestMatchers(
                                "/api/staff/auth/login",         // Staff login API public
                                "/api/admin/auth/login"          // Admin login API public
                        ).permitAll()

                        // Protected registration endpoints - Only admins can access
                        .requestMatchers("/api/staff/auth/register").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/auth/register").hasAuthority("ADMIN")

                        // Staff management endpoints - ADMIN only (MOVE THESE UP)
                        .requestMatchers("/api/staff").hasAuthority("ADMIN")
                        .requestMatchers("/api/staff/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/staff/**").hasAuthority("ADMIN")

                        // Role-based access for other endpoints
                        .requestMatchers("/api/customer/**").hasAnyAuthority("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/customer/bookings/**").hasAnyAuthority("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/admin/bookings/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/admin/tasks/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/staff/tasks/**").hasAnyAuthority("STAFF", "ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(sessionAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }}