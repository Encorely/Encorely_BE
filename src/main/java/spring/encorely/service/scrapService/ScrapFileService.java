package spring.encorely.service.scrapService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.ReviewHandler;
import spring.encorely.apiPayload.exception.handler.ScrapHandler;
import spring.encorely.apiPayload.exception.handler.UserHandler;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.scrap.ScrapFile;
import spring.encorely.domain.scrap.ScrapReview;
import spring.encorely.domain.user.User;
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

    @Transactional
    public ScrapFileResponseDTO.addFile addFile(Long userId) {
        User user = userRespository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        int suffix = 1;
        String baseName = "File";
        String generatedName = baseName + suffix;

        while (scrapFileRespository.existsByUserAndName(user, generatedName)) {
            suffix++;
            generatedName = baseName + suffix;
        }

        ScrapFile scrapFile = ScrapFile.builder()
                .user(user)
                .name(generatedName)
                .build();

        scrapFileRespository.save(scrapFile);
        return new ScrapFileResponseDTO.addFile(scrapFile.getId(), scrapFile.getName(), scrapFile.getCreatedAt());
    }
}
