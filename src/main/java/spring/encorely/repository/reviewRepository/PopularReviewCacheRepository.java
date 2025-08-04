package spring.encorely.repository.reviewRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.review.PopularReviewCache;

import java.util.List;

@Repository
public interface PopularReviewCacheRepository extends JpaRepository<PopularReviewCache, Long> {
    List<PopularReviewCache> findAll();

    @Modifying
    @Query("DELETE FROM PopularReviewCache")
    void deleteAllInBatch();

}
