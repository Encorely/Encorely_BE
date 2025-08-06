package spring.encorely.domain.show;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import spring.encorely.domain.common.BaseEntity;
import spring.encorely.domain.hall.Hall;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "shows")
public class Show extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hall hall;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String period;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String bookingDate;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String link;

}
