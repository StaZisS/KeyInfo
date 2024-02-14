package org.example.key_info.core.auth.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {
    private static final String ROLES_NAME =  "Roles";
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    private final long jwtAccessTtlSecond;
    private final long jwtRefreshTtlSecond;

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
            @Value("${jwt.ttl.access}") Long jwtAccessTtlSecond,
            @Value("${jwt.ttl.refresh}") Long jwtRefreshTtlSecond
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.jwtAccessTtlSecond = jwtAccessTtlSecond;
        this.jwtRefreshTtlSecond = jwtRefreshTtlSecond;
    }

    public String generateAccessToken(@NonNull DataForGenerateToken data) {
        final Date accessExpiration = getDateWithPlus(jwtAccessTtlSecond);
        final String accessTokenId = UUID.randomUUID().toString();
        return Jwts.builder()
                .subject(data.clientId())
                .claim(ROLES_NAME, data.role())
                .expiration(accessExpiration)
                .id(accessTokenId)
                .signWith(jwtAccessSecret)
                .compact();
    }

    public String generateRefreshToken(@NonNull DataForGenerateToken data) {
        final Date refreshExpiration = getDateWithPlus(jwtRefreshTtlSecond);
        final String refreshTokenId = UUID.randomUUID().toString();
        return Jwts.builder()
                .subject(data.clientId())
                .expiration(refreshExpiration)
                .id(refreshTokenId)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public void validateAccessToken(@NonNull String accessToken) {
        validateToken(accessToken, jwtAccessSecret);
    }

    public void validateRefreshToken(@NonNull String refreshToken) {
        validateToken(refreshToken, jwtRefreshSecret);
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    public long getRefreshTokenTtl() {
        return jwtRefreshTtlSecond;
    }

    public UUID getAccessTokenClientId(@NonNull String accessToken) {
        var claims = getAccessClaims(accessToken);
        try {
            return UUID.fromString(claims.getSubject());
        } catch (Exception e) {
            throw new ExceptionInApplication("Невозможно распарсить clientId", ExceptionType.INVALID);
        }
    }

    private Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void validateToken(@NonNull String token, @NonNull SecretKey secret) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parse(token);
        } catch (ExpiredJwtException expEx) {
            throw new ExceptionInApplication("Token expired", ExceptionType.INVALID);
        } catch (UnsupportedJwtException unsEx) {
            throw new ExceptionInApplication("Unsupported jwt", ExceptionType.INVALID);
        } catch (MalformedJwtException mjEx) {
            throw new ExceptionInApplication("Malformed jwt", ExceptionType.INVALID);
        } catch (Exception e) {
            throw new ExceptionInApplication("Invalid token", ExceptionType.INVALID);
        }
    }

    private Date getDateWithPlus(long delta) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusSeconds(delta)
                .atZone(ZoneId.systemDefault())
                .toInstant();
        return Date.from(refreshExpirationInstant);
    }
}
