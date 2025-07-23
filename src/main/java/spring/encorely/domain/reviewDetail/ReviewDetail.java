package spring.encorely.domain.reviewDetail;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import spring.encorely.domain.comment.Comment;
import spring.encorely.domain.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDetail {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Long hallId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "reviews", "comments"})
        private User user;

        @Column(length = 255)
        private String showName;

        @Column(length = 255)
        private String artistName;

        @Column(length = 30)
        private String seatArea;
        @Column(length = 30)
        private String seatRow;
        @Column(length = 30)
        private String seatNumber;

        private Float rating;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        @Column(length = 50)
        private String comment; // 후기 자체에 대한 간단한 코멘트 필드

        @Column(columnDefinition = "TEXT")
        private String detail; // 후기의 상세 내용 필드

        @Column(columnDefinition = "TEXT")
        private String seatDetail;

        private LocalDate showDate;
        private Integer round;
        private Integer viewCount;
        private Integer likeCount = 0;  // 좋아요 수 기본값 0

        private Integer commentCount; // 댓글 수 필드
        private Integer scrapCount; // 스크랩 수 필드

        @ManyToMany
        @JoinTable(
                name = "review_likes",
                joinColumns = @JoinColumn(name = "review_id"),
                inverseJoinColumns = @JoinColumn(name = "user_id")
        )
        @JsonIgnore
        private Set<User> likedUsers = new HashSet<>();

        // ⭐ 댓글 목록을 위한 OneToMany 관계 수정 ⭐
        @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference("review-comments")
        @Builder.Default
        private List<Comment> comments = new ArrayList<>(); // ⭐ commentDto -> Comment로 변경 ⭐


        public boolean isLikedBy(User user) {
                return likedUsers.contains(user);
        }

        public void like(User user) {
                if (likedUsers.add(user)) {
                        likeCount++;
                }
        }

        public void unlike(User user) {
                if (likedUsers.remove(user)) {
                        likeCount--;
                }
        }
}