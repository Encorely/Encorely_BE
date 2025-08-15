package spring.encorely.service.showService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.ShowHandler;
import spring.encorely.domain.show.Show;
import spring.encorely.dto.showDto.ShowResponseDTO;
import spring.encorely.dto.showDto.ShowSearchResponseDto;
import spring.encorely.repository.showRepository.ShowRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;

    public Show findById(Long id) {
        return showRepository.findById(id).orElseThrow(() -> new ShowHandler(ErrorStatus.SHOW_NOT_FOUND));
    }

    public List<ShowResponseDTO.GetOngoingShow> getOngoingShows() {
        LocalDate today = LocalDate.now();
        LocalDate twoMonthsLater = today.plusMonths(2);
        Pageable limit = PageRequest.of(0, 6);

        List<ShowResponseDTO.GetOngoingShow> dtos = new ArrayList<>();
        List<Show> shows = showRepository.findUpcomingShows(today, twoMonthsLater, limit);

        for (Show show : shows) {
            ShowResponseDTO.GetOngoingShow dto = ShowResponseDTO.GetOngoingShow.builder()
                    .showId(show.getId())
                    .imageUrl(show.getPosterUrl())
                    .showName(show.getName())
                    .hallId(show.getHall().getId())
                    .hallName(show.getHall().getName())
                    .startDate(show.getStartDate())
                    .endDate(show.getEndDate())
                    .build();

            dtos.add(dto);
        }
        return dtos;
    }

    public ShowResponseDTO.GetShowDetail getOngoingShow(Long showId) {
        Show show = findById(showId);

        return ShowResponseDTO.GetShowDetail.builder()
                .imageUrl(show.getPosterUrl())
                .showName(show.getName())
                .hallId(show.getHall().getId())
                .hallName(show.getHall().getName())
                .startDate(show.getStartDate())
                .endDate(show.getEndDate())
                .age(show.getAge())
                .link(show.getLink())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ShowResponseDTO.GetOngoingShow> searchShows(String searchKeyword) {
        LocalDate currentDate = LocalDate.now();
        List<Show> showList;

        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            showList = showRepository.findOngoingShows(currentDate);
        } else {
            showList = showRepository.findBySearchKeywordAndOngoing(searchKeyword, currentDate);
        }

        return showList.stream()
                .map(this::mapToShowResponseDto)
                .collect(Collectors.toList());
    }

    private ShowResponseDTO.GetOngoingShow mapToShowResponseDto(Show show) {
        return ShowResponseDTO.GetOngoingShow.builder()
                .showId(show.getId())
                .imageUrl(show.getPosterUrl())
                .showName(show.getName())
                .hallId(show.getHall().getId())
                .hallName(show.getHall().getName())
                .startDate(show.getStartDate())
                .endDate(show.getEndDate())
                .build();
    }
}
