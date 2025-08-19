package spring.encorely.repository.userRepository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.encorely.domain.user.User;
import spring.encorely.domain.user.UserBlock;

import java.util.List;
import java.util.Set;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    void deleteByBlockerAndBlocked(User blocker, User blocked);
    List<UserBlock> findAllByBlocker(User blocker);

    @Query("select ub.blocked.id from UserBlock ub where ub.blocker.id = :blockerId")
    Set<Long> findBlockedUserIdsByBlockerId(@Param("blockerId") Long blockerId);

}
