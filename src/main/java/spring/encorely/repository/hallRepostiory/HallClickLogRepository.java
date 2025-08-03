package spring.encorely.repository.hallRepostiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.encorely.domain.hall.HallClickLog;

import java.time.LocalDateTime;
import java.util.List;

public interface HallClickLogRepository extends JpaRepository<HallClickLog, Long> {

    @Query("""
    SELECT h.id AS hallId, COUNT(l.id) AS clickCount
    FROM HallClickLog l
    JOIN l.hall h
    WHERE l.createdAt >= :start
    GROUP BY h.id
    ORDER BY clickCount DESC
""")
    List<Long> findAllHallsRankingInLast7Days(@Param("start") LocalDateTime start);
}
