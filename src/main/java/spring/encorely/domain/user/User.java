package spring.encorely.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import spring.encorely.domain.common.BaseEntity;
import spring.encorely.domain.enums.Role;
import spring.encorely.domain.enums.Status;
import spring.encorely.domain.review.Review;
import spring.encorely.listener.UserEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(UserEntityListener.class)
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    @Column
    private String introduction;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private Integer historyCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.ACTIVE;

    @Column
    private LocalDate inactiveDate;

    @Column(nullable = false)
    private Integer followers = 0;

    @Column(nullable = false)
    private Integer followings = 0;

    @Column
    private String link;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    // 프로필 업데이트 메서드
    public void updateProfile(String nickname, String introduction, String link) {
        this.nickname = nickname;
        this.introduction = introduction;
        this.link = link;
    }

    // 프로필 이미지 업데이트 메서드 (별도 분리)
    public void updateProfileImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
