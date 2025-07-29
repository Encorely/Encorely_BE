package spring.encorely.service.hallService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.domain.hall.Hall;
import spring.encorely.dto.hallDto.HallResponseDto;
import spring.encorely.repository.hallRepository.HallRepository;
import spring.encorely.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HallService {

    private final HallRepository hallRepository;

    public Hall getHallEntityById(Long hallId) {
        return hallRepository.findById(hallId)
                .orElseThrow(() -> new NotFoundException("Hall not found with id: " + hallId));
    }

    public HallResponseDto getHallById(Long hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new NotFoundException("Hall not found with id: " + hallId));
        return new HallResponseDto(hall);
    }

}