package spring.encorely.repository.hallRepostiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.hall.Hall;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
}
