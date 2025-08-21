package spring.encorely.controller.authController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.tokenDto.TokenRequestDTO;
import spring.encorely.dto.tokenDto.TokenResponseDTO;
import spring.encorely.service.authService.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("logout")
    @Operation(summary = "로그아웃")
    public ApiResponse<String> logout(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestBody @Valid TokenRequestDTO.Logout request) {
        authService.logout(userDetails.getUsername(), request);
        return ApiResponse.onSuccess("로그아웃 되었습니다.");
    }

    @PostMapping("/refresh")
    @Operation(summary = "accessToken 만료 시 새 토큰 발급")
    public ApiResponse<TokenResponseDTO.NewToken> refresh(@RequestBody @Valid TokenRequestDTO.GetToken request) {
        return ApiResponse.onSuccess(authService.refreshToken(request));
    }

}
