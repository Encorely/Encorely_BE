package spring.encorely.domain.reviewDetail;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_images")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false) // ⭐ review_id로 변경
    private Review review; // ⭐ Review 엔티티 참조

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    // 편의 메서드 이름 변경
    public void setReview(Review review) { // ⭐ 메서드 이름 변경
        this.review = review;
    }
}