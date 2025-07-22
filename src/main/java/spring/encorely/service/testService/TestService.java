package spring.encorely.service.testService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import spring.encorely.config.jwt.JwtTokenUtil;
import spring.encorely.dto.testDto.TestResponseDTO;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public TestResponseDTO generateTestToken(String userId, HttpServletResponse response) {
        String accessToken = jwtTokenUtil.generateAccessToken(userId);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userId);

        UserDetails testUserDetails = new org.springframework.security.core.userdetails.User(
                userId,
                "",
                List.of(new SimpleGrantedAuthority("USER"))
        );
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUserDetails, null, testUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return new TestResponseDTO(accessToken, refreshToken);
    }

}
