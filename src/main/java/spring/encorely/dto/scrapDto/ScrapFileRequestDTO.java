package spring.encorely.dto.scrapDto;

import lombok.Getter;
import spring.encorely.domain.enums.ReviewCategory;

public class ScrapFileRequestDTO {
    @Getter
    public static class addReviewToFile{
        Long reviewId;
        ReviewCategory category;
    }

    @Getter
    public static class updateFileName {
        String name;
    }
}
