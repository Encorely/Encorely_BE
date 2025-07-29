package spring.encorely.domain.reviewDetail;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "facility_review_details")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FacilityReviewDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", unique = true, nullable = false) // Review 엔티티와의 1:1 관계
    private Review review;

    @Column(name = "facility_type")
    private String facilityType;

    @Column(name = "facility_tips", columnDefinition = "TEXT")
    private String facilityTips;

    @Enumerated(EnumType.STRING)
    @Column(name = "facility_category")
    private FacilityCategory facilityCategory;

    @Column(name = "facility_address")
    private String facilityAddress;

    @Column(name = "facility_latitude")
    private String facilityLatitude;

    @Column(name = "facility_longitude")
    private String facilityLongitude;

    @Column(name = "convenience_rating")
    private Float convenienceRating;

    @Column(name = "cleanliness_rating")
    private Float cleanlinessRating;

    public void setReview(Review review) {
        this.review = review;
    }
}