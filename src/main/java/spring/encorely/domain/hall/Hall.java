package spring.encorely.domain.hall;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "hall")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 공연장 이름

    @Column
    private String address; // 공연장 주소

    @Column
    private String imageUrl; // 공연장 대표 이미지 URL

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer clickCount = 0; // 클릭수 (초기값 0)

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 갱신 시간
}