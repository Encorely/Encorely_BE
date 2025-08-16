package spring.encorely.repository.reviewRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.enums.RestaurantType;
import spring.encorely.domain.review.Restaurant;
import spring.encorely.domain.review.Review;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByReview(Review review);

    @Query("""
        SELECT r
        FROM Restaurant r
        JOIN r.review rv
        JOIN rv.hall h
        WHERE h.id = :hallId
          AND (:keyword IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:type IS NULL OR r.restaurantType = :type)
        ORDER BY
          CASE WHEN :sort = 'latest'  THEN r.createdAt END DESC,
          CASE WHEN :sort = 'popular' THEN (rv.likeCount + rv.scrapCount) END DESC,
          r.id DESC
    """)
    Page<Restaurant> findByHallAndFilters(@Param("hallId") Long hallId,
                                          @Param("keyword") String keyword,
                                          @Param("type") RestaurantType type,
                                          @Param("sort") String sort,
                                          Pageable pageable);

}
