package spring.encorely.repository.scrapRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.scrap.ScrapFile;

public interface ScrapFileRepository extends JpaRepository<ScrapFile, Long> {
}
