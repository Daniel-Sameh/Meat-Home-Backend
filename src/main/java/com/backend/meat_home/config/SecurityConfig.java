package com.backend.meat_home.config;


import com.backend.meat_home.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/reset-password").permitAll() // public endpoints
                        .requestMatchers("/api/categories/create").hasRole("ADMIN")//Change this to the suitable role and correct api
                        .requestMatchers("/api/categories/update").hasRole("ADMIN")
                        .requestMatchers("/api/categories/delete").hasRole("ADMIN")
                        .requestMatchers("/api/categories").permitAll()
                        .requestMatchers("/api/categories/{id}").permitAll()
                        .requestMatchers("/api/orders/place").hasRole("CUSTOMER")
                        .requestMatchers("/api/orders/pending").hasAnyRole("CALL_CENTER_AGENT", "ADMIN")
                        .requestMatchers("/api/orders/confirm/{orderId}").hasRole("CALL_CENTER_AGENT")
                        .requestMatchers("/api/orders/pending").hasAnyRole("CALL_CENTER_AGENT", "ADMIN")
                        .requestMatchers("/api/orders/cancel/{orderId}").hasRole("ADMIN")
                        .requestMatchers("/api/orders/rate/{orderId}").hasRole("CUSTOMER")
                        .requestMatchers("/api/orders/visibility/{orderId}").hasRole("ADMIN")
                        .requestMatchers("/api/orders/all-orders/{customerId}").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers("/api/orders/ready").hasAnyRole("CALL_CENTER_AGENT", "ADMIN", "DRIVER")
                        .requestMatchers("/api/orders/accept/{orderId}").hasRole("DRIVER")
                        .requestMatchers("/api/orders/status/{orderId}").hasAnyRole("CALL_CENTER_AGENT", "ADMIN", "DRIVER")
                        .requestMatchers("/api/orders/track/{orderId}").hasAnyRole("CUSTOMER", "CALL_CENTER_AGENT", "ADMIN")
                        .requestMatchers("/api/enquiries/submit").hasRole("CUSTOMER")
                        .requestMatchers("/api/enquiries/all-enquiries").hasRole("ADMIN")
                        .requestMatchers("/api/enquiries/unread").hasRole("ADMIN")
                        .requestMatchers("/api/enquiries/read/{enquiryId}").hasRole("ADMIN")
                        .requestMatchers("/api/enquiries/visibility/{enquiryId}").hasRole("ADMIN")
                        .requestMatchers("/api/settings/platform", "/api/settings/terms", "/api/settings/about").permitAll()
                        .requestMatchers("/api/settings/update", "/api/settings/preview").hasRole("ADMIN")
                        .requestMatchers("/api/products/create").hasRole("ADMIN")
                        .requestMatchers("/api/products/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/{productId}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/products/{productId}").hasRole("ADMIN")
                        .requestMatchers("/api/products/delete/{productId}").hasRole("ADMIN")
                        /**
                         * Simply add any endpoint you need to be protected along with the role
                         * .requestMatchers("/api/protected/**").hasRole("role")
                         * No extra checks needed inside your controller
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
