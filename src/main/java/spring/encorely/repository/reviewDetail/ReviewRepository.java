package spring.encorely.repository.reviewDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.reviewDetail.Review;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r " +
            "LEFT JOIN FETCH r.user u " +
            "LEFT JOIN FETCH r.performanceDetail pd " +
            "LEFT JOIN FETCH pd.hall ph " +
            "LEFT JOIN FETCH r.restaurantDetail rd " +
            "LEFT JOIN FETCH r.facilityDetail fd " +
            "LEFT JOIN FETCH r.images ri " +
            "LEFT JOIN FETCH r.comments rc " +
            "LEFT JOIN FETCH rc.user rcu " +
            "LEFT JOIN FETCH r.userKeywords rk " +
            "WHERE r.id = :id")
    Optional<Review> findByIdWithDetails(@Param("id") Long id);

}