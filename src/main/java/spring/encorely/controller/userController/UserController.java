package spring.encorely.controller.userController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.userDto.UserRequestDTO;
import spring.encorely.dto.userDto.UserResponseDTO;
import spring.encorely.service.userService.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/mypage")
    public ApiResponse<UserResponseDTO.MyPage> getMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(userService.getMyPage(Long.parseLong(userDetails.getUsername())));
    }

    @PostMapping("/follow/{userId}")
    public ApiResponse<String> followUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long userId) {
        userService.followUser(Long.parseLong(userDetails.getUsername()), userId);
        return ApiResponse.onSuccess("팔로우 되었습니다.");
    }

    @PostMapping("/block/{userId}")
    public ApiResponse<String> blockUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long userId) {
        userService.blockUser(Long.parseLong(userDetails.getUsername()), userId);
        return ApiResponse.onSuccess("유저가 차단되었습니다.");
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponseDTO.UserInfo> getUserInfo(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long userId) {
        return ApiResponse.onSuccess(userService.getUserInfo(Long.parseLong(userDetails.getUsername()), userId));
    }

    @DeleteMapping("/follow/{userId}")
    public ApiResponse<String> unfollowUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long userId) {
        userService.unfollowUser(Long.parseLong(userDetails.getUsername()), userId);
        return ApiResponse.onSuccess("언팔로우 되었습니다.");
    }

    @DeleteMapping("/block/{userId}")
    public ApiResponse<String> unblockUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long userId) {
        userService.unblockUser(Long.parseLong(userDetails.getUsername()), userId);
        return ApiResponse.onSuccess("차단이 취소되었습니다.");
    }

    @GetMapping("/followers")
    public ApiResponse<List<UserResponseDTO.FollowerInfo>> getFollowers(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(userService.getFollowers(Long.parseLong(userDetails.getUsername())));
    }

    @GetMapping("/followings")
    public ApiResponse<List<UserResponseDTO.FollowingInfo>> getFollowings(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(userService.getFollowings(Long.parseLong(userDetails.getUsername())));
    }

    @PutMapping
    public ApiResponse<String> updateUser(@AuthenticationPrincipal UserDetails userDetails,
                                          @Valid @RequestBody UserRequestDTO.UpdateUser request) {
        userService.updateUser(Long.parseLong(userDetails.getUsername()), request);
        return ApiResponse.onSuccess("프로필이 수정되었습니다.");
    }

}
