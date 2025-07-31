package spring.encorely.repository.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNickname(String nickname);

    User findByProviderAndProviderId(String provider, String providerId);
    // 닉네임으로 사용자 존재 여부 확인
    boolean existsByNickname(String nickname);
}
