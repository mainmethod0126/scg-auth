package io.github.mainmethod.scgauth.auth;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TokenVerificationFilter extends OncePerRequestFilter {

    // 인증이 필요한 url 의 경우
    // 토큰이 존재하는지 확인 후 토큰이 없을 경우 튕깁니다.

    private JwtProvider jwtProvider = new JwtProvider();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (!token.startsWith("Bearer ")) {
            throw new BadCredentialsException(
                    "The value of the Authorization header should start with the word 'Bearer.' Please check and retry your request.");
        }

        String jwt = token.replace("Bearer ", "");

    }

}
