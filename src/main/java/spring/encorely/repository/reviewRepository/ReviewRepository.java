package spring.encorely.repository.reviewRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUser (User user);
    List<Review> findAllByUserIdInOrderByCreatedAtDesc(List<Long> userIds);

    @Query("SELECT r FROM Review r WHERE r.hall.id = :hallId ORDER BY r.createdAt DESC")
    Page<Review> findByHallId(@Param("hallId") Long hallId, Pageable pageable);

    @Query("""
    SELECT r
    FROM Review r
    WHERE (:hallId IS NULL OR r.hall.id = :hallId)
      AND (:seatArea IS NULL OR r.seatArea = :seatArea)
      AND (:seatRow IS NULL OR r.seatRow = :seatRow)
      AND (:seatNumber IS NULL OR r.seatNumber = :seatNumber)
    ORDER BY
      CASE WHEN :sort = 'latest' THEN r.createdAt END DESC,
      CASE WHEN :sort = 'popular' THEN (r.likeCount + r.scrapCount) END DESC
    """)
    Page<Review> findByFilters(@Param("hallId") Long hallId,
                               @Param("seatArea") String seatArea,
                               @Param("seatRow") String seatRow,
                               @Param("seatNumber") String seatNumber,
                               @Param("sort") String sort,
                               Pageable pageable);

}
