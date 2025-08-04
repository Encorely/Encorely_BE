package spring.encorely.service.userService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.UserHandler;
import spring.encorely.domain.enums.ReviewImageType;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.review.ReviewImage;
import spring.encorely.domain.user.User;
import spring.encorely.domain.user.UserBlock;
import spring.encorely.domain.user.UserFollow;
import spring.encorely.dto.userDto.UserRequestDTO;
import spring.encorely.dto.userDto.UserResponseDTO;
import spring.encorely.repository.reviewRepository.ReviewRepository;
import spring.encorely.repository.userRepository.UserBlockRepository;
import spring.encorely.repository.userRepository.UserFollowRepository;
import spring.encorely.repository.userRepository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final UserFollowRepository UserFollowRepository;
    private final UserBlockRepository userBlockRepository;
    private final UserFollowRepository userFollowRepository;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    public UserResponseDTO.MyPage getMyPage(Long id) {
        User user = findById(id);

        List<UserResponseDTO.MyEncorely> myEncorelies = new ArrayList<>();
        List<Review> reviews = reviewRepository.findAllByUser(user);

        for (Review review : reviews) {
            String imageUrl = review.getReviewImageList().stream()
                    .filter(img -> img.getType() == ReviewImageType.SHOW)
                    .map(ReviewImage::getImageUrl)
                    .findFirst()
                    .orElse(null);

            UserResponseDTO.MyEncorely myEncorely = UserResponseDTO.MyEncorely.builder()
                    .showName(review.getShowName())
                    .address(review.getHall().getAddress())
                    .imageUrl(imageUrl)
                    .build();

            myEncorelies.add(myEncorely);
        }

        List<UserFollow> userFollowings = UserFollowRepository.findAllByFollower(user);
        Set<User> followingUsers = userFollowings.stream()
                .map(UserFollow::getFollowing)
                .collect(Collectors.toSet());

        List<UserResponseDTO.FriendEncorely> friendEncorelies = new ArrayList<>();
        List<Long> followingIds = followingUsers.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        List<Review> friendsReviews = reviewRepository.findAllByUserIdInOrderByCreatedAtDesc(followingIds);

        for (Review review : friendsReviews) {
            String imageUrl = review.getReviewImageList().stream()
                    .filter(img -> img.getType() == ReviewImageType.SHOW)
                    .map(ReviewImage::getImageUrl)
                    .findFirst()
                    .orElse(null);

            User followingUser = review.getUser();

            friendEncorelies.add(UserResponseDTO.FriendEncorely.builder()
                    .imageUrl(imageUrl)
                    .profileImageUrl(followingUser.getImageUrl())
                    .nickname(followingUser.getNickname())
                    .showName(review.getShowName())
                    .hallName(review.getHall().getName())
                    .date(review.getDate())
                    .round(review.getRound())
                    .build());
        }

        return UserResponseDTO.MyPage.builder()
                .nickname(user.getNickname())
                .followers(user.getFollowers())
                .followings(user.getFollowings())
                .viewedShowCount(user.getViewedShowCount())
                .introduction(user.getIntroduction())
                .link(user.getLink())
                .myEncorelies(myEncorelies)
                .friendEncorelies(friendEncorelies)
                .build();
    }

    @Transactional
    public void followUser(Long id, Long userId) {
        User user = findById(id);
        User followUser = findById(userId);

        UserFollow userFollow = UserFollow.builder()
                .follower(user)
                .following(followUser)
                .build();

        user.setFollowings(user.getFollowings() + 1);
        followUser.setFollowers(followUser.getFollowers() + 1);

        userFollowRepository.save(userFollow);
    }

    @Transactional
    public void blockUser(Long id, Long userId) {
        User user = findById(id);
        User blockUser = findById(userId);

        UserBlock userBlock = UserBlock.builder()
                .blocker(user)
                .blocked(blockUser)
                .build();

        userBlockRepository.save(userBlock);
    }

    @Transactional
    public UserResponseDTO.UserInfo getUserInfo(Long id, Long userId) {
        User user = findById(id);
        User accessUser = findById(userId);

        List<UserResponseDTO.MyEncorely> myEncorelies = new ArrayList<>();
        List<Review> reviews = reviewRepository.findAllByUser(accessUser);

        for (Review review : reviews) {
            String imageUrl = review.getReviewImageList().stream()
                    .filter(img -> img.getType() == ReviewImageType.SHOW)
                    .map(ReviewImage::getImageUrl)
                    .findFirst()
                    .orElse(null);

            UserResponseDTO.MyEncorely myEncorely = UserResponseDTO.MyEncorely.builder()
                    .showName(review.getShowName())
                    .address(review.getHall().getAddress())
                    .imageUrl(imageUrl)
                    .build();

            myEncorelies.add(myEncorely);
        }

        boolean isFollowing = userFollowRepository.existsByFollowerAndFollowing(user, accessUser);

        return UserResponseDTO.UserInfo.builder()
                .id(accessUser.getId())
                .isFollowing(isFollowing)
                .nickname(accessUser.getNickname())
                .viewedShowCount(accessUser.getViewedShowCount())
                .followers(accessUser.getFollowers())
                .followings(accessUser.getFollowings())
                .introduction(accessUser.getIntroduction())
                .link(accessUser.getLink())
                .myEncorelies(myEncorelies)
                .build();
    }

    @Transactional
    public void unfollowUser(Long id, Long userId) {
        User user = findById(id);
        User followUser = findById(userId);

        user.setFollowings(user.getFollowings() - 1);
        followUser.setFollowers(followUser.getFollowers() - 1);

        userFollowRepository.deleteByFollowerAndFollowing(user, followUser);
    }

    @Transactional
    public void unblockUser(Long id, Long userId) {
        User user = findById(id);
        User blockUser = findById(userId);

        userBlockRepository.deleteByBlockerAndBlocked(user, blockUser);
    }

    public List<UserResponseDTO.FollowerInfo> getFollowers(Long id) {
        User user = findById(id);

        List<User> followers = userFollowRepository.findAllByFollowing(user).stream()
                .map(UserFollow::getFollower)
                .toList();

        return followers.stream()
                .map(f -> UserResponseDTO.FollowerInfo.builder()
                        .id(f.getId())
                        .imageUrl(f.getImageUrl())
                        .nickname(f.getNickname())
                        .build())
                .toList();
    }

    public List<UserResponseDTO.FollowingInfo> getFollowings(Long id) {
        User user = findById(id);

        List<User> followings = userFollowRepository.findAllByFollower(user).stream()
                .map(UserFollow::getFollowing)
                .toList();

        return followings.stream()
                .map(f -> UserResponseDTO.FollowingInfo.builder()
                        .id(f.getId())
                        .imageUrl(f.getImageUrl())
                        .nickname(f.getNickname())
                        .build())
                .toList();
    }

    public List<UserResponseDTO.BlockedInfo> getBlockedUsers(Long id) {
        User user = findById(id);

        List<User> blockedUsers = userBlockRepository.findAllByBlocker(user).stream()
                .map(UserBlock::getBlocked)
                .toList();

        return blockedUsers.stream()
                .map(f -> UserResponseDTO.BlockedInfo.builder()
                        .id(f.getId())
                        .imageUrl(f.getImageUrl())
                        .nickname(f.getNickname())
                        .build())
                .toList();
    }

    @Transactional
    public void updateUser(Long id, UserRequestDTO.UpdateUser request) {
        User user = findById(id);

        if (request.getImageUrl() != null) {
            user.setImageUrl(request.getImageUrl());
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }

        if (request.getLink() != null) {
            user.setLink(request.getLink());
        }
    }

    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

}