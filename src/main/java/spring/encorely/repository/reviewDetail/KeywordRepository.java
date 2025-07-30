package spring.encorely.repository.reviewDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.reviewDetail.UserKeyword;

public interface KeywordRepository extends JpaRepository<UserKeyword, Long> {

}