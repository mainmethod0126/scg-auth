package io.github.mainmethod.scgauth.auth;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtProvider {

    // 토큰 유효시간 30분
    @Value("${custom.token.access.expired-time:#{1800000}}")
    private long tokenValidTime;

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

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {

        var jwt = Jwts.parser().verifyWith(secretKey).build().parse(token);
        Claims claims = (Claims) jwt.getPayload();

        String id = claims.getId();
        java.util.Objects.requireNonNull(id, "The user id value obtained from the token is null");

        return id;
    }

}
