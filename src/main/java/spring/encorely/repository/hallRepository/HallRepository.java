package spring.encorely.repository.hallRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.encorely.domain.hall.Hall;

public interface HallRepository extends JpaRepository<Hall, Long> {
}
