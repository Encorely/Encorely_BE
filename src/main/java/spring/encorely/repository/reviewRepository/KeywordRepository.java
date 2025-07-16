package spring.encorely.repository.reviewRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.review.Keyword;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
}
