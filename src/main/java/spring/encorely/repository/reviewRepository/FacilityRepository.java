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

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findAllByReview(Review review);
    @Query("""
        SELECT f
        FROM Facility f
        JOIN f.review rv
        JOIN rv.hall h
        WHERE h.id = :hallId
          AND (:keyword IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:type IS NULL OR f.facilityType = :type)
        ORDER BY
          CASE WHEN :sort = 'latest'  THEN f.createdAt END DESC,
          CASE WHEN :sort = 'popular' THEN (rv.likeCount + rv.scrapCount) END DESC,
          f.id DESC
    """)
    Page<Facility> findByHallAndFilters(@Param("hallId") Long hallId,
                                        @Param("keyword") String keyword,
                                        @Param("type") FacilityType type,
                                        @Param("sort") String sort,
                                        Pageable pageable);

}
