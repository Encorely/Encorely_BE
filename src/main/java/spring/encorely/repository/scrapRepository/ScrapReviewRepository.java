package spring.encorely.repository.scrapRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.scrap.ScrapReview;

public interface ScrapReviewRepository extends JpaRepository<ScrapReview, Long> {
}
