package spring.encorely.domain.reviewDetail;

import jakarta.persistence.*;
import lombok.*;
import spring.encorely.domain.hall.Hall;

import java.time.LocalDate;

@Entity
@Table(name = "performance_review_details")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PerformanceReviewDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", unique = true, nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Column(name = "show_name")
    private String showName;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "seat_area", length = 30)
    private String seatArea;

    @Column(name = "seat_row", length = 30)
    private String seatRow;

    @Column(name = "seat_number", length = 30)
    private String seatNumber;

    @Column(name = "seat_detail", columnDefinition = "TEXT")
    private String seatDetail;

    @Column(name = "show_date")
    private LocalDate showDate;

    @Column(name = "round")
    private Integer round;

    public void setReview(Review review) {
        this.review = review;
    }
}