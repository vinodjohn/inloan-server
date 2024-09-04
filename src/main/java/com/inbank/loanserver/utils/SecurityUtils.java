package com.inbank.loanserver.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * A helper component class to provide security functionalities for this app.
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private static final SecretKey JWT_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${inLoan.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${inLoan.app.jwtCookieName}")
    private String jwtCookieName;

    @Value("${inLoan.app.jwtRefreshCookieName}")
    private String jwtRefreshCookieName;

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(JWT_SECRET_KEY)
                .compact();
    }

    public ResponseCookie generateJwtCookie(String username) {
        String jwt = generateTokenFromUsername(username);
        return generateCookie(jwtCookieName, jwt, "/");
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(jwtRefreshCookieName, refreshToken, "/auth/refresh-token");
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtCookieName);
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookieName);
    }

    public ResponseCookie getCleanJwtCookie() {
        return generateCookie(jwtCookieName, null, "/");
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        return generateCookie(jwtRefreshCookieName, null, "/auth/refresh-token");
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(JWT_SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(JWT_SECRET_KEY)
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            log.error("JWT token validation error: {}", e.getMessage());
        }
        return false;
    }

    // PRIVATE METHODS //
    private ResponseCookie generateCookie(String name, String value, String path) {
        return ResponseCookie.from(name, value)
                .path(path)
                .maxAge(86400)
                .httpOnly(true)
                .build();
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        return (cookie != null) ? cookie.getValue() : null;
    }
}