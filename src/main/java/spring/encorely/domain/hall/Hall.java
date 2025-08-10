package spring.encorely.domain.hall;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import spring.encorely.domain.common.BaseEntity;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.show.Show;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Hall extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Integer clickCount = 0;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Show> showList = new ArrayList<>();

}