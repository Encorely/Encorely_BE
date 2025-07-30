package spring.encorely.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.AuthHandler;
import spring.encorely.apiPayload.exception.handler.UserHandler;
import spring.encorely.domain.user.User;
import spring.encorely.repository.userRepository.UserRepository;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7일
    private final UserRepository userRepository;

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    private static SecretKey SECRET_KEY;

    @PostConstruct
    public void init()  {
        SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // AccessToken 생성
    public String generateAccessToken(String id) {

        User user = userRepository.findById(Long.parseLong(id)).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (user == null) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }

        return Jwts.builder()
                .setSubject(id)
                .claim("role", user.getRole().toString())
                .setId(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // RefreshToken 생성
    public String generateRefreshToken(String id) {

        User user = userRepository.findById(Long.parseLong(id)).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (user == null) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }

        return Jwts.builder()
                .setSubject(id)
                .claim("role", user.getRole().toString())
                .setId(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 검증
    public static Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthHandler(ErrorStatus.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
        }
    }

    // 토큰 만료일 추출
    public static long getExpiration(String token) {
        try {
            Claims claims = validateToken(token);
            Date expiration = claims.getExpiration();
            return expiration.getTime();
        } catch (ExpiredJwtException e) {
            throw new AuthHandler(ErrorStatus.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
        }
    }

}
