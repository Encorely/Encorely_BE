package spring.encorely.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import spring.encorely.domain.comment.Comment;
import spring.encorely.domain.common.BaseEntity;
import spring.encorely.domain.enums.Role;
import spring.encorely.domain.enums.Status;
import spring.encorely.domain.like.Like;
import spring.encorely.domain.notification.Notification;
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

    @Column
    private Integer viewedShowCount = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFollow> followerList = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFollow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "blocked", cascade = CascadeType.ALL)
    private List<UserBlock> blockedList = new ArrayList<>();

    @OneToMany(mappedBy = "blocker", cascade = CascadeType.ALL)
    private List<UserBlock> blockerList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> receiverList = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> senderList = new ArrayList<>();

}