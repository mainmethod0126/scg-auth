package io.github.mainmethod.scgauth.auth;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TokenVerificationFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;

    public TokenVerificationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    /**
     * 토큰의 유효성을 확인합니다,
     * 
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeaderValue = request.getHeader("Authorization");

        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer ")) {
            String token = authorizationHeaderValue.replace("Bearer ", "");

            if (jwtProvider.validateToken(token)) {
                var authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

}
