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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
        String id = null;
        String role = null;

        // 만료된 토큰으로 부정 접근 시
        if (accessToken != null) {
            try {
                if (Boolean.TRUE.equals(redisTemplate.hasKey("BLACKLIST" + accessToken))) {
                    throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
                }

                Claims claims =JwtTokenUtil.validateToken(accessToken);
                id = claims.getSubject();
                role = (String) claims.get("role");
                
                setAuthentication(id, role, request);

            } catch (Exception e) {
                if (refreshToken != null && Boolean.FALSE.equals(redisTemplate.hasKey("BLACKLIST" + refreshToken))) {
                    Claims refreshClaims = JwtTokenUtil.validateToken(refreshToken);
                    id = refreshClaims.getSubject();
                    role = (String) refreshClaims.get("role");

                    String storedRefresh = redisTemplate.opsForValue().get(id);
                    if (storedRefresh != null && storedRefresh.equals(refreshToken)) {
                        String newAccessToken = jwtTokenUtil.generateAccessToken(id);

                        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(Duration.ofMinutes(30))
                                .sameSite("Strict")
                                .build();

                        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

                        setAuthentication(id, role, request);

                    }
                }
            }
        } else if (refreshToken != null) {
            if (Boolean.FALSE.equals(redisTemplate.hasKey("BLACKLIST" + refreshToken))) {
                try {
                    Claims refreshClaims = JwtTokenUtil.validateToken(refreshToken);
                    id = refreshClaims.getSubject();
                    role = (String) refreshClaims.get("role");

                    String storedRefresh = redisTemplate.opsForValue().get(id);
                    if (storedRefresh != null && storedRefresh.equals(refreshToken)) {
                        String newAccessToken = jwtTokenUtil.generateAccessToken(id);

                        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(Duration.ofMinutes(30))
                                .sameSite("Strict")
                                .build();

                        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

                        setAuthentication(id, role, request);

                    }
                } catch (ExpiredJwtException e) {
                    throw new AuthHandler(ErrorStatus.TOKEN_EXPIRED);
                } catch (JwtException e) {
                    throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
                }
            }
        }

        filterChain.doFilter(request, response);
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
