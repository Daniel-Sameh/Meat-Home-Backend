package com.backend.meat_home.config;


import com.backend.meat_home.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final CustomUnauthorizedHandler customUnauthorizedHandler;
    private final CustomForbiddenHandler customForbiddenHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll() // public endpoints
                        .requestMatchers("api/categories/**").hasRole("ADMIN")//Change this to the suitable role and correct api
                        /**
                         * Simply add any endpoint you need to be protected along with the role
                         * .requestMatchers("/api/protected/**").hasRole("role")
                         * No extra checks needed inside your controller
                         *
                         * Extract the user email from the token and use it to fetch the user from the database
                         *   String email = SecurityContextHolder.getContext().getAuthentication().getName();
                         *   see: Line 42 in {@link com.backend.meat_home.controller.AuthController}
                         *   User user = userRepository.findByEmail(email);
                         */
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customUnauthorizedHandler)
                        .accessDeniedHandler(customForbiddenHandler)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
        return new JwtAuthenticationFilter(jwtUtil);
    }
}
