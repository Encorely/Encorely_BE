package spring.encorely.repository.userRepository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.encorely.domain.user.User;
import spring.encorely.domain.user.UserFollow;

import java.time.LocalDateTime;
import java.util.List;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    List<UserFollow> findAllByFollower(User user);
    boolean existsByFollowerAndFollowing(User user, User accessUser);
    void deleteByFollowerAndFollowing(User user, User accessUser);

    List<UserFollow> findAllByFollowing(User following);

    @Query("""
        SELECT uf.following.id
        FROM UserFollow uf
        WHERE uf.createdAt >= :oneWeekAgo
        GROUP BY uf.following.id
        ORDER BY COUNT(uf.id) DESC
        """)
    List<Long> findTopFollowedUsersSince(@Param("oneWeekAgo") LocalDateTime oneWeekAgo, Pageable pageable);

}
