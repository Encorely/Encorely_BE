package spring.encorely.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.AuthHandler;
import spring.encorely.config.security.CustomUserDetails;
import spring.encorely.domain.user.User;
import spring.encorely.service.userService.UserService;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            log.debug("[JwtRequestFilter] 요청 시작: {} {}", request.getMethod(), request.getRequestURI());

            String accessToken = extractTokenFromCookies(request, "accessToken");
            String refreshToken = extractTokenFromCookies(request, "refreshToken");

            if (accessToken != null) {
                handleAccessToken(accessToken, request);
            } else if (refreshToken != null) {
                tryRefreshingWithRefreshToken(refreshToken, response, request);
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.warn("Access token expired", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (JwtException e) {
            log.warn("Invalid JWT", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (AuthHandler e) {
            log.warn("AuthHandler error", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Unexpected JWT filter error", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
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
        if (refreshToken == null) {
            return;
        }
        if (isBlacklisted(refreshToken)) {
            return;
        }

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
        boolean result = Boolean.TRUE.equals(redisTemplate.hasKey("BLACKLIST" + token));
        return result;
    }

    private void setAuthentication(String userId, String role, HttpServletRequest request) {
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findById(Long.parseLong(userId));
            CustomUserDetails userDetails = new CustomUserDetails(user);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void addAccessTokenCookie(HttpServletResponse response, String newAccessToken) {
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .sameSite("None")
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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/error");
    }

}