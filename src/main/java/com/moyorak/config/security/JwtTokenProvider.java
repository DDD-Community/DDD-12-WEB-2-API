package com.moyorak.config.security;

import com.moyorak.api.auth.domain.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtTokenProperties jwtTokenProperties;

    public String generateAccessToken(final UserPrincipal user) {
        final Key key = getSigningKey(jwtTokenProperties.getSecretKey());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(user.getId().toString())
                .claim("email", user.getUsername())
                .claim("name", user.getName())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + jwtTokenProperties.getExpiration()))
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(final String token) {
        final Claims claims = parseClaims(token);

        final Long id = Long.parseLong(claims.getSubject());
        final String email = claims.get("email", String.class);
        final String name = claims.get("name", String.class);

        UserDetails userDetails = UserPrincipal.generate(id, email, name);
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());
    }

    public boolean isValidToken(final String token) {
        try {
            parseClaims(token);

            return true;
        } catch (ExpiredJwtException e) {
            log.warn("토큰이 만료 되었습니다. : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 토큰 형식입니다. : {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("잘못된 토큰 형식입니다. : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("토큰 입력이 잘못되었습니다. : {}", e.getMessage());
        } catch (Exception e) {
            log.warn("토큰 검증시 오류가 발생하였습니다. : {}", e.getMessage());
        }

        return false;
    }

    private Claims parseClaims(final String token) {
        final JwtParser parser =
                Jwts.parserBuilder()
                        .setSigningKey(getSigningKey(jwtTokenProperties.getSecretKey()))
                        .build();

        return parser.parseClaimsJws(token).getBody();
    }

    private Key getSigningKey(final String key) {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}
