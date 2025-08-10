package spring.encorely.repository.noticeRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.notification.UserNotificationSetting;
import spring.encorely.domain.user.User;

import java.util.List;

@Repository
public interface UserNotificationSettingRepository extends JpaRepository<UserNotificationSetting, Long> {
    List<UserNotificationSetting> findAllByUser(User user);
}
