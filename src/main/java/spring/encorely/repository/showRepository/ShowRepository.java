package spring.encorely.repository.showRepository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.show.Show;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    @Query("SELECT s FROM Show s WHERE s.startDate BETWEEN :today AND :twoMonthsLater ORDER BY s.startDate ASC")
    List<Show> findUpcomingShows(@Param("today") LocalDate today, @Param("twoMonthsLater") LocalDate twoMonthsLater, Pageable pageable);

}
