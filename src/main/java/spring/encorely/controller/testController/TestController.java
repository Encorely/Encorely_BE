package spring.encorely.controller.testController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.testDto.TestResponseDTO;
import spring.encorely.service.testService.TestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test-auth")
// @Profile("dev")
public class TestController {

    private final TestService testService;

    @PostMapping("/{userId}")
    public ApiResponse<TestResponseDTO> generateTestToken(@PathVariable String userId, HttpServletResponse response) {
        return ApiResponse.onSuccess(testService.generateTestToken(userId, response));
    }

}
