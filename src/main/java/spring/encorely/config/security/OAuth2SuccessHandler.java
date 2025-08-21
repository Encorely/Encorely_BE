package spring.encorely.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import spring.encorely.config.jwt.JwtTokenUtil;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("[OAuth2SuccessHandler] Authentication success triggered");

        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            log.info("Principal class: {}", oAuth2User.getClass().getName());

            Map<String, Object> attributes = oAuth2User.getAttributes();
            log.info("OAuth2User attributes: {}", attributes);

            String id = (String) attributes.get("id");
            if (id == null) {
                throw new IllegalStateException("OAuth2User does not contain 'id' attribute");
            }

            log.info("User ID: {}", id);

            String accessToken = jwtTokenUtil.generateAccessToken(id);
            String refreshToken = jwtTokenUtil.generateRefreshToken(id);
            log.info("Access Token: {}", accessToken);
            log.info("Refresh Token: {}", refreshToken);

            redisTemplate.opsForValue().set(id, refreshToken, 7, TimeUnit.DAYS);
            log.info("Refresh token saved in Redis");

            String redirectUri = String.format(
                    "encorely://oauth/kakao?accessToken=%s&refreshToken=%s",
                    accessToken,
                    refreshToken
            );

            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader(HttpHeaders.LOCATION, redirectUri);

            log.info("OAuth2 authentication success fully processed, tokens set in response");

        } catch (Exception e) {
            log.error("[OAuth2SuccessHandler] Error during OAuth2 success handling", e);
            response.sendRedirect("/login?error");
        }
    }
}