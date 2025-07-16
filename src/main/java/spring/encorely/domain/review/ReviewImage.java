package spring.encorely.domain.review;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import spring.encorely.domain.common.BaseEntity;
import spring.encorely.domain.enums.ReviewImageCategory;
import spring.encorely.domain.enums.ReviewImageType;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class ReviewImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    // 시야/공연, 맛집, 편의시설
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewImageCategory category;

    // 시야 or 공연
    @Enumerated(EnumType.STRING)
    private ReviewImageType type;

    // 시야 or 공연 사진일 때 FK
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    // 맛집 사진일 때 FK
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    // 편의시설 사진일 때 FK
    @ManyToOne(fetch = FetchType.LAZY)
    private Facility facility;

    // 실제 등록된 후기의 이미지인지 표시
    @Column(nullable = false)
    private boolean used;

    public void markAsUsed(ReviewImageCategory category, ReviewImageType reviewImageType, Review review,
                           Restaurant restaurant, Facility facility) {
        this.used = true;
        this.category = category;
        this.type = reviewImageType;
        this.review = review;
        this.restaurant = restaurant;
        this.facility = facility;
    }

}
