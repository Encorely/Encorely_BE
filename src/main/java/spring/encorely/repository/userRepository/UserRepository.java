package spring.encorely.repository.userRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.encorely.domain.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNickname(String nickname);

    User findByProviderAndProviderId(String provider, String providerId);
    // 닉네임으로 사용자 존재 여부 확인
    boolean existsByNickname(String nickname);

    @Query("SELECT u FROM User u " +
            "WHERE (u.nickname LIKE %:keyword% OR u.introduction LIKE %:keyword%) " +
            "AND u.status = 'ACTIVE' " +
            "AND u.id != :userId " +
            "AND NOT EXISTS (" +
            "    SELECT ub FROM UserBlock ub " +
            "    WHERE ub.blocker.id = :userId AND ub.blocked.id = u.id" +
            ") " +
            "AND NOT EXISTS (" +
            "    SELECT ub FROM UserBlock ub " +
            "    WHERE ub.blocker.id = u.id AND ub.blocked.id = :userId" +
            ")")
    Page<User> findUserByKeyword(Long userId, String keyword, Pageable pageable);
}
