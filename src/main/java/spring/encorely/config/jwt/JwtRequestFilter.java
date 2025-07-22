package spring.encorely.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.AuthHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = extractTokenFromCookies(request, "accessToken");
        String refreshToken = extractTokenFromCookies(request, "refreshToken");

        if (accessToken != null) {
            try {
                handleAccessToken(accessToken, request);
            } catch (Exception e) {
                tryRefreshingWithRefreshToken(refreshToken, response, request);
            }
        } else if (refreshToken != null) {
            tryRefreshingWithRefreshToken(refreshToken, response, request);
        }

        filterChain.doFilter(request, response);
    }

    private void handleAccessToken(String accessToken, HttpServletRequest request) {
        if (isBlacklisted(accessToken)) {
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
        }

        Claims claims = JwtTokenUtil.validateToken(accessToken);
        String id = claims.getSubject();
        String role = (String) claims.get("role");

        setAuthentication(id, role, request);
    }

    private void tryRefreshingWithRefreshToken(String refreshToken, HttpServletResponse response, HttpServletRequest request) {
        if (refreshToken == null || isBlacklisted(refreshToken)) return;

        try {
            Claims refreshClaims = JwtTokenUtil.validateToken(refreshToken);
            String id = refreshClaims.getSubject();
            String role = (String) refreshClaims.get("role");

            String storedRefresh = redisTemplate.opsForValue().get(id);
            if (refreshToken.equals(storedRefresh)) {
                String newAccessToken = jwtTokenUtil.generateAccessToken(id);
                addAccessTokenCookie(response, newAccessToken);
                setAuthentication(id, role, request);
            }
        } catch (ExpiredJwtException e) {
            throw new AuthHandler(ErrorStatus.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
        }
    }

    private boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("BLACKLIST" + token));
    }

    private void setAuthentication(String userId, String role, HttpServletRequest request) {
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void addAccessTokenCookie(HttpServletResponse response, String newAccessToken) {
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    }

    private String extractTokenFromCookies(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}