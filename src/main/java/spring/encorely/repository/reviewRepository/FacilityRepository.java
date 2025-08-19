package spring.encorely.repository.reviewRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.enums.FacilityType;
import spring.encorely.domain.review.Facility;
import spring.encorely.domain.review.Review;

import java.util.List;
import java.util.Set;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findAllByReview(Review review);

    @Query("""
    SELECT f FROM Facility f
    WHERE f.review.hall.id = :hallId
        AND (:keyword IS NULL OR f.name LIKE %:keyword%)
        AND (:type IS NULL OR f.facilityType = :type)
        AND (:blockedIds IS NULL OR f.review.user.id NOT IN :blockedIds)
    ORDER BY
        CASE WHEN :sort = 'latest' THEN f.createdAt END DESC,
        CASE WHEN :sort = 'popular' THEN (f.review.likeCount + f.review.scrapCount) END DESC
    """)
    Page<Facility> findByHallAndFiltersExcludingBlocked(@Param("hallId") Long hallId,
                                                        @Param("keyword") String keyword,
                                                        @Param("type") FacilityType type,
                                                        @Param("sort") String sort,
                                                        @Param("blockedIds") Set<Long> blockedIds,
                                                        Pageable pageable);

}
