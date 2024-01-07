package io.github.mainmethod.scgauth.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import io.github.mainmethod.scgauth.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private JwtProvider jwtProvider;

    public JwtLoginSuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    /**
     * 인증이 성공하였으니 Jwt를 발급합니다.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        var principal = authentication.getPrincipal();

        if (principal instanceof User) {
            User loginUser = (User) principal;
            String accessToken = this.jwtProvider.createAccessToken(loginUser.getUserId());
            String refreshToken = this.jwtProvider.createRefreshToken();

            response.addHeader("Authorization", "Bearer " + accessToken);
            response.getWriter().write(refreshToken);
            return;
        }

        throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
    }

}
