package spring.encorely.service.scrapService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.ReviewHandler;
import spring.encorely.apiPayload.exception.handler.ScrapHandler;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.scrap.ScrapFile;
import spring.encorely.domain.scrap.ScrapReview;
import spring.encorely.dto.scrapDto.ScrapFileRequestDTO;
import spring.encorely.dto.scrapDto.ScrapFileResponseDTO;
import spring.encorely.repository.reviewRepository.ReviewRepository;
import spring.encorely.repository.scrapRepository.ScrapFileRepository;
import spring.encorely.repository.scrapRepository.ScrapReviewRepository;
import spring.encorely.repository.userRepository.UserRepository;

@Service
@RequiredArgsConstructor
public class ScrapFileService {
    private final ScrapReviewRepository scrapReviewRespository;
    private final ScrapFileRepository scrapFileRespository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRespository;

    @Transactional
    public ScrapFileResponseDTO.addReviewToFile addReviewToFile(Long fileId, ScrapFileRequestDTO.addReviewToFile request) {
        ScrapFile scrapFile = scrapFileRespository.findById(fileId).orElseThrow(() -> new ScrapHandler(ErrorStatus.SCRAP_FILE_NOT_FOUND));
        Review review = reviewRepository.findById(request.getReviewId()).orElseThrow(() -> new ReviewHandler(ErrorStatus.REVIEW_NOT_FOUND));
        

        ScrapReview scrapReview = ScrapReview.builder()
                .scrapFile(scrapFile)
                .review(review)
                .category(request.getCategory())
                .build();

        scrapReviewRespository.save(scrapReview);
        return new ScrapFileResponseDTO.addReviewToFile(scrapReview.getId(), scrapReview.getCreatedAt());
    }
}
