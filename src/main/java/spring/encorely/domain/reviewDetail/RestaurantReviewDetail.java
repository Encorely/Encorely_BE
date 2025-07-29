package spring.encorely.domain.reviewDetail;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurant_review_details")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RestaurantReviewDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", unique = true, nullable = false)
    private Review review;

    @Column(name = "place_name")
    private String placeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "restaurant_category")
    private RestaurantCategory restaurantCategory;

    @Column(name = "restaurant_address")
    private String restaurantAddress;

    @Column(name = "restaurant_latitude")
    private String restaurantLatitude;

    @Column(name = "restaurant_longitude")
    private String restaurantLongitude;

    @Column(name = "brand_name")
    private String brandName;

    public void setReview(Review review) {
        this.review = review;
    }
}