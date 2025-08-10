package spring.encorely.repository.scrapRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.scrap.ScrapFile;
import spring.encorely.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface ScrapFileRepository extends JpaRepository<ScrapFile, Long> {
    boolean existsByUserAndName(User user, String name);
    Optional<ScrapFile> findByUserAndIsDefault(User user, boolean isDefault);
    List<ScrapFile> findAllByUser(User user);
}
