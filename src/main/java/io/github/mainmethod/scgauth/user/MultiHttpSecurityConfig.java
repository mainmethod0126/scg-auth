package io.github.mainmethod.scgauth.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.github.mainmethod.scgauth.auth.JsonUsernamePasswordAuthenticationFilter;
import io.github.mainmethod.scgauth.auth.JwtProvider;
import io.github.mainmethod.scgauth.auth.TokenVerificationFilter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class MultiHttpSecurityConfig {

    private final UserDetailsService userService;

    // 토큰 유효시간 30분
    @Value("${custom.token.access.expired-time:#{1800000}}")
    private long tokenValidTime;

    public JwtProvider jwtProvider(UserDetailsService userService) {
        return new JwtProvider(userService, tokenValidTime);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(requests -> requests
                .requestMatchers("/", "/signup", "/home").permitAll()
                .anyRequest().authenticated())
                .addFilterBefore(
                        new JsonUsernamePasswordAuthenticationFilter(
                                authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                                new JwtProvider(this.userService, this.tokenValidTime)),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new TokenVerificationFilter(new JwtProvider(this.userService, this.tokenValidTime)),
                        JsonUsernamePasswordAuthenticationFilter.class)
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }

}
