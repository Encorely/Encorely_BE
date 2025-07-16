package spring.encorely.service.reviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.ReviewHandler;
import spring.encorely.domain.review.Keyword;
import spring.encorely.domain.review.Review;
import spring.encorely.repository.reviewRepository.KeywordRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public Keyword findById(Long id) {
        return keywordRepository.findById(id).orElseThrow(() -> new ReviewHandler(ErrorStatus.KEYWORDS_NOT_FOUND));
    }

}
