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
import java.util.Set;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByReview(Review review);

    @Query("""
    SELECT r FROM Restaurant r
    WHERE r.review.hall.id = :hallId
      AND (:keyword IS NULL OR r.name LIKE %:keyword%)
      AND (:type IS NULL OR r.restaurantType = :type)
      AND (COALESCE(:blockedIds) IS NULL OR r.review.user.id NOT IN :blockedIds)
    ORDER BY
      CASE WHEN :sort = 'latest' THEN r.createdAt END DESC,
      CASE WHEN :sort = 'popular' THEN (r.review.likeCount + r.review.scrapCount) END DESC
    """)
    Page<Restaurant> findByHallAndFiltersExcludingBlocked(@Param("hallId") Long hallId,
                                                          @Param("keyword") String keyword,
                                                          @Param("type") RestaurantType type,
                                                          @Param("sort") String sort,
                                                          @Param("blockedIds") Set<Long>  blockedIds,
                                                          Pageable pageable);

}
