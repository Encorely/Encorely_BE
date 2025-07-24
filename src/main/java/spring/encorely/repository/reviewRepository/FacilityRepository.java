package spring.encorely.repository.reviewRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.review.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
