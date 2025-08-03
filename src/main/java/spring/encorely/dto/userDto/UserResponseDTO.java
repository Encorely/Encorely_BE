package spring.encorely.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPage {

        String nickname;
        Integer followers;
        Integer followings;
        Integer viewedShowCount;
        String introduction;
        String link;
        List<MyEncorely> myEncorelies;
        List<FriendEncorely> friendEncorelies;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyEncorely {

        String showName;
        String address;
        String imageUrl;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FriendEncorely {

        String imageUrl;
        String profileImageUrl;
        String nickname;
        String showName;
        String hallName;
        Date date;
        Integer round;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {

        Long id;
        String nickname;
        boolean isFollowing;
        Integer viewedShowCount;
        Integer followers;
        Integer followings;
        String introduction;
        String link;
        List<MyEncorely> myEncorelies;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowerInfo {

        Long id;
        String imageUrl;
        String nickname;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowingInfo {

        Long id;
        String imageUrl;
        String nickname;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BlockedInfo {

        Long id;
        String imageUrl;
        String nickname;

    }

}