package spring.encorely.service.authService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(String userId, String refreshToken, Long expirationMillis) {
        redisTemplate.opsForValue().set(
                "refreshToken:" + userId,
                refreshToken,
                expirationMillis,
                TimeUnit.MILLISECONDS
        );
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get("refreshToken:" + userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(userId);
    }

}
