package spring.encorely.repository.userRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.encorely.domain.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNickname(String nickname);

    User findByProviderAndProviderId(String provider, String providerId);
    // 닉네임으로 사용자 존재 여부 확인
    boolean existsByNickname(String nickname);

    @Query("""
        SELECT u
        FROM User u
        WHERE (
            LOWER(u.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(COALESCE(u.introduction, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (:userId IS NULL OR NOT EXISTS (
            SELECT 1 FROM UserBlock b
            WHERE b.blocker.id = :userId
              AND b.blocked.id = u.id
        ))
        AND (:userId IS NULL OR u.id <> :userId)
        ORDER BY u.id DESC
    """)
    Page<User> findUserByKeyword(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

}
