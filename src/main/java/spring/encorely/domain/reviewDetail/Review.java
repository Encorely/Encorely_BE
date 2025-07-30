package spring.encorely.domain.reviewDetail;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import spring.encorely.domain.comment.Comment;
import spring.encorely.domain.hall.Hall;
import spring.encorely.domain.reviewDetail.ReviewImage;
import spring.encorely.domain.user.User;
import spring.encorely.domain.reviewDetail.UserKeyword;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @Enumerated(EnumType.STRING)
        @Column(name = "review_category_type")
        private ReviewCategory reviewCategoryType;

        @Column(nullable = false)
        private Float rating;

        @Column(length = 50)
        private String comment;

        @Column(columnDefinition = "TEXT")
        private String detail;

        @Column(name = "view_count", columnDefinition = "INTEGER DEFAULT 0")
        private Integer viewCount = 0;

        @Column(name = "like_count", columnDefinition = "INTEGER DEFAULT 0")
        private Integer likeCount = 0;

        @Column(name = "comment_count", columnDefinition = "INTEGER DEFAULT 0")
        private Integer commentCount = 0;

        @Column(name = "scrap_count", columnDefinition = "INTEGER DEFAULT 0")
        private Integer scrapCount = 0;

        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at", nullable = false)
        private LocalDateTime updatedAt;

        @Column(name = "visit_date")
        private LocalDate visitDate;

        // PerformanceReviewDetail
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "performance_hall_id")
        private Hall performanceHall;
        @Column(name = "performance_show_name")
        private String performanceShowName;
        @Column(name = "performance_artist_name")
        private String performanceArtistName;
        @Column(name = "performance_seat_area")
        private String performanceSeatArea;
        @Column(name = "performance_seat_row")
        private String performanceSeatRow;
        @Column(name = "performance_seat_number")
        private String performanceSeatNumber;
        @Column(name = "performance_seat_detail", columnDefinition = "TEXT")
        private String performanceSeatDetail;
        @Column(name = "performance_show_date")
        private LocalDate performanceShowDate;
        @Column(name = "performance_round")
        private Integer performanceRound;

        // RestaurantReviewDetail
        @Column(name = "restaurant_place_name")
        private String restaurantPlaceName;
        @Column(name = "restaurant_category")
        private String restaurantCategory;
        @Column(name = "restaurant_address")
        private String restaurantAddress;
        @Column(name = "restaurant_latitude")
        private Float restaurantLatitude;
        @Column(name = "restaurant_longitude")
        private Float restaurantLongitude;
        @Column(name = "restaurant_brand_name")
        private String restaurantBrandName;

        //FacilityReviewDetail
        @Column(name = "facility_type")
        private String facilityType;
        @Column(name = "facility_tips", columnDefinition = "TEXT")
        private String facilityTips;
        @Column(name = "facility_category")
        private String facilityCategory;
        @Column(name = "facility_address")
        private String facilityAddress;
        @Column(name = "facility_latitude")
        private Double facilityLatitude;
        @Column(name = "facility_longitude")
        private Double facilityLongitude;
        @Column(name = "facility_convenience_rating")
        private Float facilityConvenienceRating;
        @Column(name = "facility_cleanliness_rating")
        private Float facilityCleanlinessRating;


        @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private Set<ReviewImage> images = new HashSet<>();

        @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private Set<Comment> comments = new HashSet<>();

        @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private Set<UserKeyword> userKeywords = new HashSet<>();

        public void addImage(ReviewImage image) {
                this.images.add(image);
                image.setReview(this);
        }

        public void addUserKeyword(UserKeyword userKeyword) {
                this.userKeywords.add(userKeyword);
                userKeyword.setReview(this);
        }


        public void incrementLikeCount() {
                this.likeCount++;
        }

        public void decrementLikeCount() {
                if (this.likeCount > 0) {
                        this.likeCount--;
                }
        }
}