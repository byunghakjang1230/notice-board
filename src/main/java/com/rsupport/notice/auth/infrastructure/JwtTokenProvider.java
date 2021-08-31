package com.rsupport.notice.auth.infrastructure;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rsupport.notice.auth.exception.AuthorizationException;

import io.jsonwebtoken.*;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;
    private JwtParser jwtParser;

    @Autowired
    public JwtTokenProvider(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    public String createToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plus(validityInMilliseconds, ChronoUnit.MILLIS);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(validity))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getEmailBy(String token) {
        validateUsableToken(token);
        return this.jwtParser
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validateUsableToken(String token) {
        try {
            this.jwtParser
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(toDate(LocalDateTime.now()));
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthorizationException("사용가능한 토큰이 아닙니다.");
        }
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}

