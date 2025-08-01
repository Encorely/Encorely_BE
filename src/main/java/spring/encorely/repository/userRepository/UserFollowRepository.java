package spring.encorely.repository.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.user.User;
import spring.encorely.domain.user.UserFollow;

import java.util.List;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    List<UserFollow> findAllByFollower(User user);
    boolean existsByFollowerAndFollowing(User user, User accessUser);
    void deleteByFollowerAndFollowing(User user, User accessUser);

    List<UserFollow> findAllByFollowing(User following);
}
