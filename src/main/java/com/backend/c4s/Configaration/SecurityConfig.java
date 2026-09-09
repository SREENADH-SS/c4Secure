package com.backend.c4s.Configaration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CorsConfigurationSource corsConfigurationSource;

    private static final String[] PUBLIC_URLS = {
            "/error",
            "/api/v1/auth",
            "/api/v1/auth/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false); // keep it ONLY inside the Spring Security chain via addFilterBefore
        return registrationBean;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 1. Allow browser/Postman CORS pre-flight OPTIONS requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. Allow auth & documentation endpoints
                        .requestMatchers(PUBLIC_URLS).permitAll()

                        // 3. Allow public GET read-only access to products, categories, and brands
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/products/**",
                                "/api/v1/categories/**",
                                "/api/v1/brands/**",
                                "/api/v1/product-images/**"
                        ).permitAll()

                        // 4. Role-restricted routes
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/cart/**").hasAnyRole("USER", "ADMIN")

                        // 5. Secure remaining endpoints
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
