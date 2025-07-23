package spring.encorely.domain.comment; // domain 패키지 안의 comment 패키지에 위치

import com.fasterxml.jackson.annotation.JsonBackReference; // JSON 직렬화 역참조용 임포트
import com.fasterxml.jackson.annotation.JsonIgnore; // JSON 직렬화 무시용 임포트
import com.fasterxml.jackson.annotation.JsonManagedReference; // JSON 직렬화 정참조용 임포트
import jakarta.persistence.*; // JPA 관련 어노테이션 임포트
import lombok.*; // Lombok 어노테이션 임포트
import org.hibernate.annotations.CreationTimestamp; // 생성 시간 자동 기록용 임포트
import org.hibernate.annotations.UpdateTimestamp; // 업데이트 시간 자동 기록용 임포트
import spring.encorely.domain.reviewDetail.ReviewDetail; // ReviewDetail 엔티티 임포트
import spring.encorely.domain.user.User; // User 엔티티 임포트

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // 이 클래스가 JPA 엔티티임을 나타냅니다.
@Table(name = "comment") // 이 엔티티가 매핑될 데이터베이스 테이블 이름을 "comment"로 지정합니다.
@Getter // 모든 필드에 대한 getter 메서드를 자동으로 생성합니다.
@Setter // 모든 필드에 대한 setter 메서드를 자동으로 생성합니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 인자 없는 생성자를 자동으로 생성합니다. protected 접근 제어자로 설정하여 외부에서의 직접 생성을 막습니다.
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자를 자동으로 생성합니다.
@Builder // 빌더 패턴을 사용하여 객체를 생성할 수 있도록 합니다.
public class Comment {

    @Id // 이 필드가 엔티티의 기본 키(Primary Key)임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임합니다 (Auto-increment).
    private Long id; // 댓글의 고유 ID

    @ManyToOne(fetch = FetchType.LAZY) // ReviewDetail 엔티티와의 다대일(N:1) 관계를 설정합니다. 지연 로딩(Lazy Loading)으로 설정합니다.
    @JoinColumn(name = "review_id", nullable = false) // 외래 키 컬럼 이름을 "review_id"로 지정하고, 필수 값으로 설정합니다.
    // ReviewDetail 엔티티의 comments 필드에 @JsonManagedReference("review-comments")가 있어야 순환 참조를 방지할 수 있습니다.
    @JsonBackReference("review-comments") // JSON 직렬화 시 이 관계의 "역방향"임을 나타냅니다. 즉, 댓글 직렬화 시 ReviewDetail 객체의 comments 필드를 포함하지 않도록 합니다.
    private ReviewDetail review; // 이 댓글이 속한 후기 엔티티

    @ManyToOne(fetch = FetchType.LAZY) // User 엔티티와의 다대일(N:1) 관계를 설정합니다. 지연 로딩(Lazy Loading)으로 설정합니다.
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 컬럼 이름을 "user_id"로 지정하고, 필수 값으로 설정합니다.
    @JsonIgnore // JSON 직렬화 시 이 필드를 완전히 무시합니다. (댓글 조회 시 사용자 전체 정보가 필요 없을 때 사용)
    // 만약 User 정보가 필요하고 User 엔티티에 Comment 목록 필드가 있다면,
    // User 엔티티의 해당 필드에 @JsonManagedReference("user-comments")를 설정하고
    // 여기에 @JsonBackReference("user-comments")를 사용해야 합니다.
    private User user; // 댓글을 작성한 사용자 엔티티

    @Column(columnDefinition = "TEXT", nullable = false) // 데이터베이스 컬럼 타입을 TEXT로 지정하고, 필수 값으로 설정합니다.
    private String content; // 댓글 내용

    @CreationTimestamp // 엔티티가 데이터베이스에 처음 저장될 때 현재 시간을 자동으로 기록합니다.
    @Column(nullable = false) // 필수 값으로 설정합니다.
    private LocalDateTime createdAt; // 댓글 생성 시간

    @UpdateTimestamp // 엔티티가 업데이트될 때마다 현재 시간을 자동으로 갱신합니다.
    private LocalDateTime updatedAt; // 댓글 마지막 업데이트 시간

    @ManyToOne(fetch = FetchType.LAZY) // 부모 Comment 엔티티와의 다대일(N:1) 관계를 설정합니다 (대댓글 기능). 지연 로딩으로 설정합니다.
    @JoinColumn(name = "parent_id") // 외래 키 컬럼 이름을 "parent_id"로 지정합니다. 최상위 댓글은 부모가 없으므로 null을 허용합니다.
    @JsonBackReference("parent-children") // JSON 직렬화 시 부모-자식 관계의 "역방향"임을 나타냅니다. 자식 댓글 직렬화 시 부모 댓글의 children 필드를 포함하지 않도록 합니다.
    private Comment parent; // 이 댓글의 부모 댓글 엔티티 (최상위 댓글인 경우 null)

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true) // 자식 Comment 엔티티(대댓글)와의 일대다(1:N) 관계를 설정합니다.
    // mappedBy = "parent": Comment 엔티티의 parent 필드에 의해 매핑됨을 나타냅니다.
    // cascade = CascadeType.ALL: 부모 댓글 작업 시 모든 자식 댓글에도 작업이 전파됩니다.
    // orphanRemoval = true: 부모 댓글에서 자식 댓글이 제거되면 해당 자식 댓글도 DB에서 삭제됩니다.
    @Builder.Default // Lombok Builder 사용 시 이 필드의 기본값을 ArrayList로 설정합니다.
    @JsonManagedReference("parent-children") // JSON 직렬화 시 부모-자식 관계의 "정방향"임을 나타냅니다. 부모 댓글 직렬화 시 자식 댓글 목록을 포함합니다.
    private List<Comment> children = new ArrayList<>(); // 이 댓글에 달린 대댓글 목록 엔티티

    // 편의 메서드: 댓글 생성 또는 업데이트 시 ReviewDetail과 User 엔티티를 연결합니다.
    public void setReviewAndUser(ReviewDetail review, User user) {
        this.review = review;
        this.user = user;
    }
}