package io.github.mainmethod.scgauth.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MultiHttpSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(config -> {
            config.disable();
        })
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/signup", "/home").permitAll()
                        .anyRequest().authenticated())
                .logout(logout -> logout.permitAll());
        return http.build();
    }

}
