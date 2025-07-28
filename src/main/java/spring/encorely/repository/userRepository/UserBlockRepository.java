package spring.encorely.repository.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.user.User;
import spring.encorely.domain.user.UserBlock;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    void deleteByBlockerAndBlocked(User blocker, User blocked);
}
