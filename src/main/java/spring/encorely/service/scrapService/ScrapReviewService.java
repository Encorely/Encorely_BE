package spring.encorely.service.scrapService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.domain.enums.ReviewCategory;
import spring.encorely.domain.enums.ReviewImageCategory;
import spring.encorely.domain.enums.ReviewImageType;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.review.ReviewImage;
import spring.encorely.domain.scrap.ScrapReview;
import spring.encorely.dto.scrapDto.ScrapFileResponseDTO;
import spring.encorely.repository.reviewRepository.ReviewImageRepository;
import spring.encorely.repository.reviewRepository.UserKeywordsRepository;
import spring.encorely.repository.scrapRepository.ScrapReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapReviewService {

    private final ScrapReviewRepository scrapReviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final UserKeywordsRepository userKeywordsRepository;

    public ScrapFileResponseDTO.GetScrapReview getScrapReviews(Long fileId, Long hallId, List<ReviewCategory> categories, String sort) {
        List<ScrapReview> scrapReviews = scrapReviewRepository.findByFilters(fileId, hallId,
                categories != null && !categories.isEmpty() ? categories : null, sort);

        List<ScrapFileResponseDTO.ReviewInfo> reviewInfos = scrapReviews.stream()
                .map(scrapReview -> {
                    Review review = scrapReview.getReview();
                    ReviewImageCategory targetCategory = mapToImageCategory(scrapReview.getCategory());

                    List<String> imageUrls = reviewImageRepository
                            .findAllByReviewAndUsedTrue(review)
                            .stream()
                            .sorted((i1, i2) -> {
                                boolean i1Matches = mathcesPriority(targetCategory, i1);
                                boolean i2Matches = mathcesPriority(targetCategory, i2);

                                if (i1Matches && !i2Matches) return -1;
                                if (!i1Matches && i2Matches) return 1;

                                return i2.getCreatedAt().compareTo(i1.getCreatedAt());
                            })
                            .map(ReviewImage::getImageUrl)
                            .collect(Collectors.toList());

                    String representativeKeyword = userKeywordsRepository.findTop1ByReviewOrderByCreatedAtAsc(review)
                            .map(uk -> uk.getKeyword().getContent())
                            .orElse(null);

                    return ScrapFileResponseDTO.ReviewInfo.builder()
                            .reviewId(review.getId())
                            .userId(review.getUser().getId())
                            .userProfileImageUrl(review.getUser().getImageUrl())
                            .nickname(review.getUser().getNickname())
                            .seatArea(review.getSeatArea())
                            .seatRow(review.getSeatRow())
                            .seatNumber(review.getSeatNumber())
                            .scrapCount(review.getScrapCount())
                            .rating(review.getRating())
                            .imageUrls(imageUrls)
                            .showDetail(review.getShowDetail())
                            .representativeKeyword(representativeKeyword)
                            .numOfKeywords(review.getKeywordList().size() - 1)
                            .build();
                })
                .toList();

        return ScrapFileResponseDTO.GetScrapReview.builder()
                .numOfReviews(reviewInfos.size())
                .reviews(reviewInfos)
                .build();
    }

    private ReviewImageCategory mapToImageCategory(ReviewCategory category) {
        return switch (category) {
            case VIEW -> ReviewImageCategory.REVIEW;
            case RESTAURANT -> ReviewImageCategory.RESTAURANT;
            case FACILITY -> ReviewImageCategory.FACILITY;
        };
    }

    private boolean mathcesPriority(ReviewImageCategory targetCategory, ReviewImage image) {
        if (targetCategory == ReviewImageCategory.REVIEW) {
            return image.getCategory() == ReviewImageCategory.REVIEW && image.getType() == ReviewImageType.VIEW;
        } else {
            return image.getCategory() == targetCategory;
        }
    }

}
