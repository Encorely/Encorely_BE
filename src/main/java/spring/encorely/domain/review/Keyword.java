package spring.encorely.domain.review;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import spring.encorely.domain.common.BaseEntity;
import spring.encorely.domain.enums.KeywordsType;

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
public class Keyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private KeywordsType keywordsType;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserKeywords> userKeywordsList  = new ArrayList<>();

}
