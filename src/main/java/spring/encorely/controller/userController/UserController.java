// src/main/java/spring/encorely/controller/userController/UserController.java

package spring.encorely.controller.userController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.userDto.CommonResponseDto; // CommonResponseDto import
import spring.encorely.service.userService.UserService;
import spring.encorely.dto.userDto.UserProfileUpdateRequestDto;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/{userId}/profile")
    public ApiResponse<Object> updateProfile(
            @PathVariable Long userId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("data") UserProfileUpdateRequestDto requestDto
    ) throws IOException {
        userService.updateProfile(userId, requestDto, image);
        return ApiResponse.onSuccess(null);
    }

    @GetMapping("/nickname/duplicate")
    public ApiResponse<Object> checkNicknameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = userService.checkNicknameDuplicate(nickname);

        if (isDuplicate) {
            return ApiResponse.onSuccess(new CommonResponseDto(false, "중복된 닉네임입니다."));
        } else {
            return ApiResponse.onSuccess(new CommonResponseDto(true, "사용 가능한 닉네임입니다."));
        }
    }
}