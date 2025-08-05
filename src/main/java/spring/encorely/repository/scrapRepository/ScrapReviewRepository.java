package spring.encorely.repository.scrapRepository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.encorely.domain.enums.ReviewCategory;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.scrap.ScrapFile;
import spring.encorely.domain.scrap.ScrapReview;
import spring.encorely.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface ScrapReviewRepository extends JpaRepository<ScrapReview, Long> {
    Optional<ScrapReview> findByReviewAndScrapFile_User(Review review, User user);
    List<ScrapReview> findAllByScrapFile(ScrapFile scrapFile);
    void deleteByReviewAndScrapFile_User(Review review, User user);

    @Query("""
        SELECT sr
        FROM ScrapReview sr
        JOIN FETCH sr.review r
        JOIN FETCH r.user u
        WHERE sr.scrapFile.id = :scrapFileId
        AND (:hallId IS NULL OR r.hall.id = :hallId)
        AND (:categories IS NULL OR sr.category IN :categories)
        ORDER BY
            CASE WHEN :sort = 'latest' THEN r.createdAt END DESC,
            CASE WHEN :sort = 'popular' THEN (r.likeCount + r.scrapCount) END DESC
    """)
    List<ScrapReview> findByFilters(@Param("scrapFileId") Long scrapFileId, @Param("hallId") Long hallId,
                                    @Param("categories")List<ReviewCategory> categories, @Param("sort") String sort);

}
