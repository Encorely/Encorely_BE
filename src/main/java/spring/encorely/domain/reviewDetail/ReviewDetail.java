package spring.encorely.domain.reviewDetail;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import spring.encorely.domain.user.User;
import spring.encorely.domain.comment.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReviewDetail {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private Long hallId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @Column(nullable = false)
        private String showName;

        @Column(nullable = false)
        private String artistName;

        @Column
        private String seatArea;

        @Column
        private String seatRow;

        @Column
        private String seatNumber;

        @Column(nullable = false)
        private Float rating;

        @CreationTimestamp
        @Column(nullable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        @Column(columnDefinition = "TEXT")
        private String comment;

        @Column(columnDefinition = "TEXT")
        private String detail;

        @Column
        private String seatDetail;

        @Column(nullable = false)
        private LocalDate showDate;

        @Column
        private Integer round;

        @Column(columnDefinition = "INTEGER DEFAULT 0")
        private Integer viewCount = 0;

        @Column(columnDefinition = "INTEGER DEFAULT 0")
        private Integer likeCount = 0;

        @Column(columnDefinition = "INTEGER DEFAULT 0")
        private Integer commentCount = 0;

        @Column(columnDefinition = "INTEGER DEFAULT 0")
        private Integer scrapCount = 0;

        @OneToMany(mappedBy = "reviewDetail", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<ReviewImage> images = new ArrayList<>();

        @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<Comment> comments = new ArrayList<>();

        public void addImage(ReviewImage image) {
                this.images.add(image);
                image.setReviewDetail(this);
        }

        public void removeImage(ReviewImage image) {
                this.images.remove(image);
                image.setReviewDetail(null);
        }

        public void addUser(User user) {
                this.user = user;
        }

        public void addComment(Comment comment) {
                this.comments.add(comment);
                comment.setReview(this);
        }
}