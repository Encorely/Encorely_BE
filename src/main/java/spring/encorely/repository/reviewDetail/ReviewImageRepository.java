package spring.encorely.repository.reviewDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.reviewDetail.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    }
