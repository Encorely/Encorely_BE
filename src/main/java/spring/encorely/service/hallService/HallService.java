package spring.encorely.service.hallService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.HallHandler;
import spring.encorely.domain.hall.Hall;
import spring.encorely.domain.hall.HallClickRanking;
import spring.encorely.dto.hallDto.HallResponseDTO;
import spring.encorely.dto.hallDto.HallSearchResponseDto;
import spring.encorely.exception.NotFoundException;
import spring.encorely.repository.hallRepostiory.HallClickLogRepository;
import spring.encorely.repository.hallRepostiory.HallClickRankingRepository;
import spring.encorely.repository.hallRepostiory.HallRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HallService {

    private final HallRepository hallRepository;
    private final HallClickLogRepository hallClickLogRepository;
    private final HallClickRankingRepository hallClickRankingRepository;

    public Hall findById(Long id) {
        return hallRepository.findById(id).orElseThrow(() -> new HallHandler(ErrorStatus.HALL_NOT_FOUND));
    }

    @Scheduled(cron = "0 0 12 * * MON") // 매주 월요일 낮 12시
    //@Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void updateWeeklyRanking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);

        List<Long> rankings = hallClickLogRepository.findAllHallsRankingInLast7Days(oneWeekAgo);

        int rank = 1;
        for (Long ranking : rankings) {
            Hall hall = hallRepository.findById(ranking).orElseThrow(() -> new HallHandler(ErrorStatus.HALL_NOT_FOUND));

            HallClickRanking newRanking = HallClickRanking.builder()
                    .hall(hall)
                    .ranking(rank++)
                    .rankingDate(LocalDate.now())
                    .build();
            hallClickRankingRepository.save(newRanking);
        }
    }

    @Transactional
    public HallResponseDTO.HallRankingList getHallList(Pageable pageable) {
        Page<HallClickRanking> hallClickRankingPage = hallClickRankingRepository.findLatestRanking(pageable);

        List<HallResponseDTO.HallRanking> hallRankingList = hallClickRankingPage.getContent().stream()
                .map(hallClickRanking -> HallResponseDTO.HallRanking.builder()
                        .name(hallClickRanking.getHall().getName())
                        .hallImageUrl(hallClickRanking.getHall().getImageUrl())
                        .ranking(hallClickRanking.getRanking())
                        .address(hallClickRanking.getHall().getAddress())
                        .build())
                .toList();

        return HallResponseDTO.HallRankingList.builder()
                .hallRankingList(hallRankingList)
                .build();
    }

    @Transactional
    public HallResponseDTO.HallRankingList getHallRankingList() {
        List<HallClickRanking> hallClickRankingList = hallClickRankingRepository.findTop6LatestRanking();
        List<HallResponseDTO.HallRanking> hallRankingList = new ArrayList<>();
        for (HallClickRanking hallClickRanking : hallClickRankingList) {
            HallResponseDTO.HallRanking hallRanking = HallResponseDTO.HallRanking.builder()
                    .name(hallClickRanking.getHall().getName())
                    .hallImageUrl(hallClickRanking.getHall().getImageUrl())
                    .ranking(hallClickRanking.getRanking())
                    .address(hallClickRanking.getHall().getAddress())
                    .build();
            hallRankingList.add(hallRanking);
        }

        return HallResponseDTO.HallRankingList.builder()
                .hallRankingList(hallRankingList)
                .build();
    }

    @Transactional(readOnly = true)
    public List<HallSearchResponseDto> searchHalls(String searchKeyword) {
        List<Hall> hallList;

        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            hallList = hallRepository.findAll();
        } else {
            hallList = hallRepository.findByNameOrAddressContaining(searchKeyword);
        }

        return hallList.stream()
                .map(HallSearchResponseDto::from)
                .collect(Collectors.toList());
    }

}
