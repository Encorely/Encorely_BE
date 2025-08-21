package spring.encorely.service.authService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import spring.encorely.config.jwt.JwtTokenUtil;
import spring.encorely.dto.tokenDto.TokenRequestDTO;
import spring.encorely.dto.tokenDto.TokenResponseDTO;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final ValueOperations<String, String> redisOps;
    private final TokenService tokenService;

    public void logout(String id, TokenRequestDTO.Logout request) {
        String refreshToken = redisOps.getOperations().opsForValue().get(id);

        if (refreshToken != null && refreshToken.equals(request.getRefreshToken())) {
            redisOps.getOperations().delete(id);

            long refreshExpiration = jwtTokenUtil.getExpiration(refreshToken);
            redisOps.getOperations().opsForValue().set("BLACKLIST" + refreshToken, "logout", refreshExpiration, TimeUnit.MILLISECONDS);
        }

        long accessExpiration = jwtTokenUtil.getExpiration(request.getAccessToken());
        redisOps.getOperations().opsForValue().set("BLACKLIST" + request.getAccessToken(), "logout", accessExpiration, TimeUnit.MILLISECONDS);

    }

    public TokenResponseDTO.NewToken refreshToken(TokenRequestDTO.GetToken request) {
        String refreshToken = request.getRefreshToken();

        Claims claims = JwtTokenUtil.validateToken(refreshToken);
        String userId = claims.getSubject();

        String storedRefresh = tokenService.getRefreshToken(userId);
        if (storedRefresh == null || !refreshToken.equals(storedRefresh)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtTokenUtil.generateAccessToken(userId);

        return TokenResponseDTO.NewToken.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
