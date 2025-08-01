package spring.encorely.repository.scrapRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.scrap.ScrapFile;
import spring.encorely.domain.user.User;

public interface ScrapFileRepository extends JpaRepository<ScrapFile, Long> {
    boolean existsByUserAndName(User user, String name);
}
