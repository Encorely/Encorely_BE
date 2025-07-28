package spring.encorely.dto.reviewDetailDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.encorely.domain.reviewDetail.ReviewDetail;
import spring.encorely.domain.reviewDetail.ReviewImage;
import spring.encorely.dto.commentDto.CommentResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDetailResponseDto {

    private Long id;
    private Long hallId;
    private Long userId;
    private String userName;
    private String showName;
    private String artistName;
    private String seatArea;
    private String seatRow;
    private String seatNumber;
    private Float rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String comment;
    private String detail;
    private String seatDetail;
    private LocalDate showDate;
    private Integer round;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer scrapCount;
    private boolean isLikedByCurrentUser;

    private List<String> imageUrls;

    private List<CommentResponseDto> comments;

    public ReviewDetailResponseDto(ReviewDetail reviewDetail) {
        this.id = reviewDetail.getId();
        this.hallId = reviewDetail.getHallId();
        this.userId = reviewDetail.getUser() != null ? reviewDetail.getUser().getId() : null;
        this.userName = reviewDetail.getUser() != null ? reviewDetail.getUser().getNickname() : null;
        this.showName = reviewDetail.getShowName();
        this.artistName = reviewDetail.getArtistName();
        this.seatArea = reviewDetail.getSeatArea();
        this.seatRow = reviewDetail.getSeatRow();
        this.seatNumber = reviewDetail.getSeatNumber();
        this.rating = reviewDetail.getRating();
        this.createdAt = reviewDetail.getCreatedAt();
        this.updatedAt = reviewDetail.getUpdatedAt();
        this.comment = reviewDetail.getComment();
        this.detail = reviewDetail.getDetail();
        this.seatDetail = reviewDetail.getSeatDetail();
        this.showDate = reviewDetail.getShowDate();
        this.round = reviewDetail.getRound();
        this.viewCount = reviewDetail.getViewCount();
        this.likeCount = reviewDetail.getLikeCount();
        this.commentCount = reviewDetail.getCommentCount();
        this.scrapCount = reviewDetail.getScrapCount();
        this.isLikedByCurrentUser = false;

        if (reviewDetail.getImages() != null) {
            this.imageUrls = reviewDetail.getImages().stream()
                    .map(ReviewImage::getImageUrl)
                    .collect(Collectors.toList());
        } else {
            this.imageUrls = new ArrayList<>();
        }

        if (reviewDetail.getComments() != null) {
            this.comments = reviewDetail.getComments().stream()
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        }
    }

    public void setIsLikedByCurrentUser(boolean isLikedByCurrentUser) {
        this.isLikedByCurrentUser = isLikedByCurrentUser;
    }
}