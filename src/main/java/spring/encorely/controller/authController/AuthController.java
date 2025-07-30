package spring.encorely.controller.authController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.service.authService.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("logout")
    public ApiResponse<String> logout(@AuthenticationPrincipal UserDetails userDetails,
                                      HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response, userDetails.getUsername());
        return ApiResponse.onSuccess("로그아웃 되었습니다.");
    }
}
