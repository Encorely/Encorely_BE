package spring.encorely.controller.userController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.userDto.CommonResponseDto;
import spring.encorely.dto.userDto.UserRequestDTO;
import spring.encorely.dto.userDto.UserResponseDTO;
import spring.encorely.service.notificationService.NotificationService;
import spring.encorely.service.notificationService.UserNotificationSettingService;
import spring.encorely.service.userService.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/mypage")
    @Operation(summary = "마이페이지 불러오기(My Encorely, Friend Encorely 포함")
    public ApiResponse<UserResponseDTO.MyPage> getMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(userService.getMyPage(Long.parseLong(userDetails.getUsername())));
    }

    @PostMapping("/follow/{userId}")
    @Operation(summary = "사용자 팔로우")
    public ApiResponse<String> followUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long userId) {
        userService.followUser(Long.parseLong(userDetails.getUsername()), userId);
        return ApiResponse.onSuccess("팔로우 되었습니다.");
    }

    @PostMapping("/block/{userId}")
    @Operation(summary = "사용자 차단")
    public ApiResponse<String> blockUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long userId) {
        userService.blockUser(Long.parseLong(userDetails.getUsername()), userId);
        return ApiResponse.onSuccess("유저가 차단되었습니다.");
    }

    @GetMapping("/{userId}")
    @Operation(summary = "특정 유저 프로필 보기")
    public ApiResponse<UserResponseDTO.UserInfo> getUserInfo(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long userId) {
        return ApiResponse.onSuccess(userService.getUserInfo(Long.parseLong(userDetails.getUsername()), userId));
    }

    @DeleteMapping("/follow/{userId}")
    @Operation(summary = "사용자 언팔로우")
    public ApiResponse<String> unfollowUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long userId) {
        userService.unfollowUser(Long.parseLong(userDetails.getUsername()), userId);
        return ApiResponse.onSuccess("언팔로우 되었습니다.");
    }

    @DeleteMapping("/block/{userId}")
    @Operation(summary = "사용자 차단 취소")
    public ApiResponse<String> unblockUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long userId) {
        userService.unblockUser(Long.parseLong(userDetails.getUsername()), userId);
        return ApiResponse.onSuccess("차단이 취소되었습니다.");
    }

    @GetMapping("/followers")
    @Operation(summary = "팔로워 리스트 불러오기")
    public ApiResponse<List<UserResponseDTO.FollowerInfo>> getFollowers(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(userService.getFollowers(Long.parseLong(userDetails.getUsername())));
    }

    @GetMapping("/followings")
    @Operation(summary = "팔로잉 리스트 불러오기")
    public ApiResponse<List<UserResponseDTO.FollowingInfo>> getFollowings(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(userService.getFollowings(Long.parseLong(userDetails.getUsername())));
    }

    @GetMapping("/blockedList")
    @Operation(summary = "차단된 계정 불러오기")
    public ApiResponse<List<UserResponseDTO.BlockedInfo>> getBlockedUsers(@AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.onSuccess(userService.getBlockedUsers(Long.parseLong(userDetails.getUsername())));
    }

    @PutMapping
    @Operation(summary = "유저 프로필 수정")
    public ApiResponse<String> updateUser(@AuthenticationPrincipal UserDetails userDetails,
                                          @Valid @RequestBody UserRequestDTO.UpdateUser request) {
        userService.updateUser(Long.parseLong(userDetails.getUsername()), request);
        return ApiResponse.onSuccess("프로필이 수정되었습니다.");
    }

    @GetMapping("/nickname/duplicate")
    @Operation(summary = "닉네임 중복 확인")
    public ApiResponse<Object> checkNicknameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = userService.checkNicknameDuplicate(nickname);

        if (isDuplicate) {
            return ApiResponse.onSuccess(new CommonResponseDto(false, "중복된 닉네임입니다."));
        } else {
            return ApiResponse.onSuccess(new CommonResponseDto(true, "사용 가능한 닉네임입니다."));
        }
    }

    @GetMapping("/userRanking")
    @Operation(summary = "인기 사용자 불러오기")
    public ApiResponse<List<UserResponseDTO.PopularUserInfo>> getPopularUsers() {
        return ApiResponse.onSuccess(userService.getPopularUsers());
    }

    @DeleteMapping
    @Operation(summary = "회원탈퇴")
    public ApiResponse<String> deleteUser(@AuthenticationPrincipal UserDetails userDetails,
                                          HttpServletRequest request) {
        userService.deleteUser(Long.parseLong(userDetails.getUsername()), request);
        return ApiResponse.onSuccess("탈퇴가 완료되었습니다.");
    }

    @GetMapping("/searching/{keyword}")
    @Operation(summary = "사용자 검색")
    public ApiResponse<List<UserResponseDTO.PopularUserInfo>> searchUsers(@AuthenticationPrincipal UserDetails userDetails,
                                                                          @PathVariable String keyword,
                                                                          Pageable pageable) {
        return ApiResponse.onSuccess(userService.searchUsers(4L, keyword, pageable));
    }

}