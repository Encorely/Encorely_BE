package spring.encorely.service.authService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import spring.encorely.config.jwt.JwtTokenUtil;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final ValueOperations<String, String> redisOps;

    public void logout(HttpServletRequest request, HttpServletResponse response, String id) {
        String accessToken = extractTokenFromCookies(request, "accessToken");
        String refreshToken = redisOps.getOperations().opsForValue().get(id);

        if (refreshToken != null) {
            redisOps.getOperations().delete(id);

            long refreshExpiration = jwtTokenUtil.getExpiration(refreshToken);
            redisOps.getOperations().opsForValue().set("BLACKLIST" + refreshToken, "logout", refreshExpiration, TimeUnit.SECONDS);
        }

        long accessExpiration = jwtTokenUtil.getExpiration(accessToken);
        redisOps.getOperations().opsForValue().set("BLACKLIST" + accessToken, "logout", accessExpiration, TimeUnit.SECONDS);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)  // 만료 시간을 0으로 설정해서 삭제
                .sameSite("Strict")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)  // 만료 시간을 0으로 설정해서 삭제
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
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
