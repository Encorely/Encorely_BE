package spring.encorely.service.hallService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.HallHandler;
import spring.encorely.domain.hall.Hall;
import spring.encorely.repository.hallRepostiory.HallRepository;

@Service
@RequiredArgsConstructor
public class HallService {

    private final HallRepository hallRepository;

    public Hall findById(Long id) {
        return hallRepository.findById(id).orElseThrow(() -> new HallHandler(ErrorStatus.HALL_NOT_FOUND));
    }

}
