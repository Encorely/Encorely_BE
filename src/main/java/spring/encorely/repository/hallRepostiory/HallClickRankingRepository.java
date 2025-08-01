package spring.encorely.repository.hallRepostiory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.encorely.domain.hall.HallClickRanking;

import java.util.List;

public interface HallClickRankingRepository extends JpaRepository<HallClickRanking, Long> {


    @Query("SELECT h FROM HallClickRanking h WHERE h.rankingDate = (SELECT MAX(h2.rankingDate) FROM HallClickRanking h2)")
    Page<HallClickRanking> findLatestRanking(Pageable pageable);

    @Query("SELECT h FROM HallClickRanking h WHERE h.rankingDate = (SELECT MAX(h2.rankingDate) FROM HallClickRanking h2) " +
            "AND h.ranking BETWEEN 1 AND 6")
    List<HallClickRanking> findTop6LatestRanking();

}
