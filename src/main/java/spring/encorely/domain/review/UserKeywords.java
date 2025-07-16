package spring.encorely.domain.review;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import spring.encorely.domain.common.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class UserKeywords extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 후기 키워드일 때 FK
    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    // 맛집 키워드일 때 FK
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    private Keyword keyword;
}
