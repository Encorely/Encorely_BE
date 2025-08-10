package spring.encorely.domain.scrap;

import jakarta.persistence.*;
import lombok.*;
import spring.encorely.domain.common.BaseEntity;
import spring.encorely.domain.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "scrap_file", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "name"})
})
public class ScrapFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "scrapFile", cascade = CascadeType.ALL)
    private List<ScrapReview> reviewScraps = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private boolean isDefault = false;

    public void changeName(String newName) {
        this.name = newName;
    }
}