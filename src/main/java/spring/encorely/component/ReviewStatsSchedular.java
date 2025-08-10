package spring.encorely.component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.encorely.domain.review.PopularReviewCache;
import spring.encorely.domain.review.Review;
import spring.encorely.repository.reviewRepository.PopularReviewCacheRepository;
import spring.encorely.repository.reviewRepository.ReviewRepository;
import spring.encorely.repository.reviewRepository.ReviewStatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewStatsSchedular {

    private final ReviewRepository reviewRepository;
    private final ReviewStatsRepository reviewStatsRepository;
    private final PopularReviewCacheRepository popularReviewCacheRepository;

    @Scheduled(cron = "0 0 12 * * MON")
    @Transactional
    public void updatePopularReviewCache() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastWeek = now.minusWeeks(1);

        List<Object[]> result = reviewStatsRepository.findTopReviewIncreases(lastWeek, now, PageRequest.of(0, 6));

        popularReviewCacheRepository.deleteAllInBatch();

        for (Object[] row : result) {
            Review review = (Review) row[0];
            popularReviewCacheRepository.save(
                    PopularReviewCache.builder()
                            .review(review)
                            .snapshotTime(now)
                            .build()
            );
        }
    }

}
