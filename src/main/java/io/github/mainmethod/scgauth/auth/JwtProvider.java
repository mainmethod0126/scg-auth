package io.github.mainmethod.scgauth.auth;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtProvider {

    private UserDetailsService userService;
    private long tokenValidTime;

    public JwtProvider(UserDetailsService userService, long tokenValidTime) {
        this.userService = userService;
        this.tokenValidTime = tokenValidTime;
    }

    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    private String createTokenId() {
        return Integer.toString(new Date().hashCode());
    }

    // JWT Acceess 토큰 생성
    public String createAccessToken(String userPk) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + tokenValidTime);

        return Jwts.builder().signWith(secretKey).claims().expiration(exp).issuedAt(now)
                .subject(userPk)
                .id(createTokenId())
                .and()
                .compact();

    }

    /**
     * Access 토큰 연장을 위한 refresh 토큰 생성
     */
    public String createRefreshToken() {

        Date now = new Date();

        return Jwts.builder().signWith(secretKey).claims().issuedAt(now).id(createTokenId())
                .and()
                .compact();

    }

    /**
     * 토큰에서 사용자 id 추출
     * 
     * @param token
     * @return
     */
    public String getUserPk(String token) {

        var jwt = Jwts.parser().verifyWith(secretKey).build().parse(token);
        Claims claims = (Claims) jwt.getPayload();
        String id = claims.getId();
        java.util.Objects.requireNonNull(id, "The user id value obtained from the token is null");

        return id;
    }

    /**
     * 토큰의 유효성 확인
     */
    public boolean validateToken(String token) {

        long now = new Date().getTime();

        var jwt = Jwts.parser().verifyWith(secretKey).build().parse(token);
        Claims claims = (Claims) jwt.getPayload();

        return now < claims.getExpiration().getTime();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
