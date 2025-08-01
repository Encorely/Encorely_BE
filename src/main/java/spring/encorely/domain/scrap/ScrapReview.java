package spring.encorely.domain.scrap;

import jakarta.persistence.*;
import lombok.*;
import spring.encorely.domain.common.BaseEntity;
import spring.encorely.domain.enums.ReviewCategory;
import spring.encorely.domain.review.Review;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ScrapReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private ReviewCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fileId")
    private ScrapFile scrapFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId")
    private Review review;
}
