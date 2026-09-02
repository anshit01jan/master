package com.scrum.auth.security;

import com.scrum.auth.filter.InputValidationFilter;
import com.scrum.auth.filter.RateLimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final InputValidator inputValidator;
    private final RateLimitFilter rateLimitFilter;

    public SecurityConfig(InputValidator inputValidator, RateLimitFilter rateLimitFilter) {
        this.inputValidator = inputValidator;
        this.rateLimitFilter = rateLimitFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/auth/login", "/auth/register", "/auth/forgot-password",
                                "/auth/reset-password", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/dashboard", "/logout").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/auth/login")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())
                .xssProtection()
                .and()
                .contentSecurityPolicy("default-src 'self'")
            );

        http.addFilterBefore(new InputValidationFilter(inputValidator), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordHasher createHasher() {
        return new PasswordHasher(12);
    }
}
