package spring.encorely.repository.hallRepostiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.encorely.domain.hall.Hall;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {

    // 공연장 이름이나 주소에 검색 키워드가 포함된 공연장을 찾는 쿼리
    @Query("SELECT h FROM Hall h WHERE h.name LIKE %:searchKeyword% OR h.address LIKE %:searchKeyword%")
    List<Hall> findByNameOrAddressContaining(@Param("searchKeyword") String searchKeyword);

    // 검색어가 없을 경우 전체 목록을 반환
    List<Hall> findAll();
}
