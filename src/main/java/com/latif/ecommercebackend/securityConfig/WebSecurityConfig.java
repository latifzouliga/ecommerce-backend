package com.latif.ecommercebackend.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JWTRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // check where the code is coming from, like is it the same front end
        http.csrf(AbstractHttpConfigurer::disable);
        // only accepts requests from example.com
        http.cors(AbstractHttpConfigurer::disable);

        // adding a filter in our case is jwtRequestFilter and this filter needs to be before AuthorizationFilter.class
        // simply run jwtRequestFilter and then run any filter that is implementing AuthorizationFilter
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/product",
                        "/auth/register",
                        "/auth/login",
                        "/auth/verify",
                        "/swagger-ui/**",
                        "/v3/api-docs/**")
                .permitAll()
                .anyRequest()
                .authenticated());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


}
