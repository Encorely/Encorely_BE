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

    // 현재 날짜 기준으로 진행 중인 공연을 조회
    @Query("SELECT s FROM Show s WHERE :currentDate BETWEEN s.startDate AND s.endDate")
    List<Show> findOngoingShows(@Param("currentDate") LocalDate currentDate);

    // 검색 키워드로 공연 조회
    @Query("SELECT s FROM Show s WHERE " +
            "(:searchKeyword IS NULL OR " +
            "(:searchKeyword LIKE CONCAT('%', REPLACE(:searchKeyword, ' ', '%'), '%') AND " +
            "(s.name LIKE CONCAT('%', REPLACE(:searchKeyword, ' ', '%'), '%') OR " +
            "s.hall.name LIKE CONCAT('%', REPLACE(:searchKeyword, ' ', '%'), '%') OR " +
            "s.hall.address LIKE CONCAT('%', REPLACE(:searchKeyword, ' ', '%'), '%')))) " +
            "AND :currentDate BETWEEN s.startDate AND s.endDate")
    List<Show> findBySearchKeywordAndOngoing(@Param("searchKeyword") String searchKeyword, @Param("currentDate") LocalDate currentDate);
}
