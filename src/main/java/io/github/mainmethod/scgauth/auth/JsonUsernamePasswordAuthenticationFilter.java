package io.github.mainmethod.scgauth.auth;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JsonUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();
        SignInRequestDto signInRequestDto;
        try {
            signInRequestDto = objectMapper.readValue(request.getInputStream(), SignInRequestDto.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "The request body format for authentication appears to be invalid. Please verify the validity of the body",
                    e);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                signInRequestDto.getId(), signInRequestDto.getPassword());

        return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);

    }
}
