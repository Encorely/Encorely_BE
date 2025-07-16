package spring.encorely.domain.review;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import spring.encorely.domain.common.BaseEntity;
import spring.encorely.domain.hall.Hall;
import spring.encorely.domain.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Integer round;

    @Column(nullable = false)
    private String showName;

    @Column(nullable = false)
    private String artistName;

    @Column(nullable = false, length = 30)
    private String seatArea;

    @Column(nullable = false, length = 30)
    private String seatRow;

    @Column(nullable = false, length = 30)
    private String seatNumber;
    
    @Column(nullable = false)
    private Float rating;

    @Column(nullable = false, length = 50)
    private String comment;

    private String showDetail;

    @Column(nullable = false)
    private Integer viewCount = 0;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @Column(nullable = false)
    private Integer commentCount = 0;

    @Column(nullable = false)
    private Integer scrapCount = 0;

    private String seatDetail;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keyword> keywordList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImageList  = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Restaurant> restaurantList  = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Facility> facilityList  = new ArrayList<>();

}
