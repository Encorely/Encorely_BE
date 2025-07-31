package spring.encorely.service.userService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.UserHandler;
import spring.encorely.domain.user.User;
import spring.encorely.dto.userDto.UserProfileUpdateRequestDto;
import spring.encorely.repository.userRepository.UserRepository;
import spring.encorely.service.s3Service.S3Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    @Transactional
    public User updateProfile(Long userId, UserProfileUpdateRequestDto requestDto, MultipartFile profileImage) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 닉네임 변경 시 중복 확인 로직 추가
        if (requestDto.getNickname() != null && !requestDto.getNickname().equals(user.getNickname())) {
            if (userRepository.existsByNickname(requestDto.getNickname())) {
                throw new UserHandler(ErrorStatus.NICKNAME_DUPLICATE); // 중복 닉네임 예외 발생
            }
        }

        // 프로필 이미지 업데이트 로직
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = s3Service.uploadFile(profileImage);
            user.updateProfileImageUrl(imageUrl);
        }

        // 프로필 정보 업데이트
        user.updateProfile(requestDto.getNickname(), requestDto.getIntroduction(), requestDto.getLink());
        return user;
    }

    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}