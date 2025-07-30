package spring.encorely.domain.reviewDetail;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import spring.encorely.domain.comment.Comment;
import spring.encorely.domain.hall.Hall;
import spring.encorely.domain.like.Like;
import spring.encorely.domain.user.User;

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
        @Column(name = "review_category_type", nullable = false)
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

        @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        private PerformanceReviewDetail performanceDetail;

        @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        private RestaurantReviewDetail restaurantDetail;

        @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        private FacilityReviewDetail facilityDetail;

        @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private Set<ReviewImage> images = new HashSet<>();

        @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true) // Comment 엔티티와 연결
        @Builder.Default
        private Set<Comment> comments = new HashSet<>();

        @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private Set<Like> likes = new HashSet<>();

        @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private Set<UserKeyword> userKeywords = new HashSet<>();

        // 편의 메서드
        public void addImage(ReviewImage image) {
                this.images.add(image);
                image.setReview(this);
        }

        public void addUserKeyword(UserKeyword userKeyword) {
                this.userKeywords.add(userKeyword);
                userKeyword.setReview(this);
        }

        public void setPerformanceDetail(PerformanceReviewDetail performanceDetail) {
                this.performanceDetail = performanceDetail;
                if (performanceDetail != null) {
                        performanceDetail.setReview(this);
                }
        }

        public void setRestaurantDetail(RestaurantReviewDetail restaurantDetail) {
                this.restaurantDetail = restaurantDetail;
                if (restaurantDetail != null) {
                        restaurantDetail.setReview(this);
                }
        }

        public void setFacilityDetail(FacilityReviewDetail facilityDetail) {
                this.facilityDetail = facilityDetail;
                if (facilityDetail != null) {
                        facilityDetail.setReview(this);
                }
        }

        // 좋아요 수 증가
        public void incrementLikeCount() {
                this.likeCount++;
        }

        // 좋아요 수 감소
        public void decrementLikeCount() {
                if (this.likeCount > 0) { // 음수 방지
                        this.likeCount--;
                }
        }
}