package spring.encorely.repository.reviewRepository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.review.ReviewStats;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewStatsRepository extends JpaRepository<ReviewStats, Long> {

    @Query("""
        SELECT rs1.review, 
               (rs2.likeCount - rs1.likeCount + rs2.commentCount - rs1.commentCount + rs2.scrapCount - rs1.scrapCount) AS diff
        FROM ReviewStats rs1
        JOIN ReviewStats rs2 ON rs1.review = rs2.review
        WHERE rs1.createdAt = :oneWeekAgo AND rs2.createdAt = :today
        ORDER BY diff DESC
        """)
    List<Object[]> findTopReviewIncreases(
            @Param("oneWeekAgo") LocalDateTime oneWeekAgo,
            @Param("today") LocalDateTime today,
            Pageable pageable
    );

    List<ReviewStats> findAllByCreatedAt(LocalDateTime createdAt);

    @Query("SELECT MAX(rs.createdAt) FROM ReviewStats rs")
    LocalDateTime findLatestSnapshotDate();

}
