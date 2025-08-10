package spring.encorely.service.reviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.domain.review.Keyword;
import spring.encorely.domain.review.Restaurant;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.review.UserKeywords;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.repository.reviewRepository.UserKeywordsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserKeywordsService {

    private final UserKeywordsRepository userKeywordsRepository;
    private final KeywordService keywordService;

    @Transactional
    public void saveHallKeywords(Review review, List<Long> idList) {
        for (Long id : idList) {
            Keyword keyword = keywordService.findById(id);

            UserKeywords userKeywords = UserKeywords.builder()
                    .keyword(keyword)
                    .review(review)
                    .build();

            userKeywordsRepository.save(userKeywords);
        }
    }

    @Transactional
    public void saveRestaurantKeywords(Restaurant restaurant, List<Long> idList) {
        for (Long id : idList) {
            Keyword keyword = keywordService.findById(id);

            UserKeywords userKeywords = UserKeywords.builder()
                    .keyword(keyword)
                    .restaurant(restaurant)
                    .build();

            userKeywordsRepository.save(userKeywords);
        }
    }

    public List<ReviewResponseDTO.Keyword> getKeywords(Review review, Restaurant restaurant) {
        List<ReviewResponseDTO.Keyword> dtos = new ArrayList<>();
        List<UserKeywords> userKeywords = new ArrayList<>();

        if (review != null) {
            userKeywords = userKeywordsRepository.findAllByReviewOrderByCreatedAtAsc(review);
        } else {
            userKeywords = userKeywordsRepository.findAllByRestaurantOrderByCreatedAtAsc(restaurant);
        }

        for (UserKeywords userKeyword : userKeywords) {
            ReviewResponseDTO.Keyword keyword = ReviewResponseDTO.Keyword.builder()
                    .keywordId(userKeyword.getId())
                    .content(userKeyword.getKeyword().getContent())
                    .build();

            dtos.add(keyword);
        }

        return dtos;
    }

}
