package spring.encorely.domain.hall;

import jakarta.persistence.*;
import lombok.*;
import spring.encorely.domain.common.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HallClickRanking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Hall hall;

    @Column(nullable = false)
    private Integer ranking;

    @Column(nullable = false)
    private LocalDate rankingDate;
}
