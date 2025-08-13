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

    @Query("SELECT DISTINCT r FROM Review r " +
            "LEFT JOIN r.user u " +
            "LEFT JOIN r.hall h " +
            "LEFT JOIN r.restaurantList rest " +
            "LEFT JOIN r.facilityList fac " +
            "WHERE " +
            "(r.showName LIKE %:keyword% " +
            "OR r.artistName LIKE %:keyword% " +
            "OR r.seatArea LIKE %:keyword% " +
            "OR r.seatRow LIKE %:keyword% " +
            "OR r.seatNumber LIKE %:keyword% " +
            "OR r.comment LIKE %:keyword% " +
            "OR r.showDetail LIKE %:keyword% " +
            "OR u.nickname LIKE %:keyword% " +
            "OR h.name LIKE %:keyword% " +
            "OR rest.name LIKE %:keyword% " +
            "OR fac.name LIKE %:keyword%)" +
            "AND u.status = 'ACTIVE' " +
            "AND u.id != :userId " +
            "AND NOT EXISTS (" +
            "    SELECT ub FROM UserBlock ub " +
            "    WHERE ub.blocker.id = :userId AND ub.blocked.id = u.id" +
            ") " +
            "AND NOT EXISTS (" +
            "    SELECT ub FROM UserBlock ub " +
            "    WHERE ub.blocker.id = u.id AND ub.blocked.id = :userId" +
            ")")
    Page<Review> findReviewByKeyword(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
}
