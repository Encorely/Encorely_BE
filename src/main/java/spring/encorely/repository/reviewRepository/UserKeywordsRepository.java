package spring.encorely.repository.reviewRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.review.UserKeywords;

@Repository
public interface UserKeywordsRepository extends JpaRepository<UserKeywords, Long> {
}
