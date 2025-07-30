package spring.encorely.dto.commentDto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.encorely.domain.comment.Comment;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long reviewId;
    private Long userId;
    private String userNickname;
    private String timeAgo;

    @JsonBackReference("parent-children")
    private CommentResponseDto parent;

    @JsonManagedReference("parent-children")
    private List<CommentResponseDto> children;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.reviewId = comment.getReview() != null ? comment.getReview().getId() : null;
        this.userId = comment.getUser() != null ? comment.getUser().getId() : null;
        this.userNickname = comment.getUser() != null ? comment.getUser().getNickname() : null;
        this.timeAgo = calculateTimeAgo(comment.getCreatedAt());

        if (comment.getChildren() != null && !comment.getChildren().isEmpty()) {
            this.children = comment.getChildren().stream()
                    .map(childComment -> {
                        CommentResponseDto childDto = new CommentResponseDto(childComment);
                        childDto.setParent(null);
                        return childDto;
                    })
                    .collect(Collectors.toList());
        } else {
            this.children = null;
        }

        this.parent = null;
    }

    public void setParent(CommentResponseDto parent) {
        this.parent = parent;
    }

    private String calculateTimeAgo(LocalDateTime createdAt) {
        if (createdAt == null) {
            return "";
        }

        LocalDateTime now = LocalDateTime.now();
        long diffSeconds = ChronoUnit.SECONDS.between(createdAt, now);

        if (diffSeconds < 60) {
            return "방금 전";
        } else if (diffSeconds < 3600) { // 1시간 미만
            return ChronoUnit.MINUTES.between(createdAt, now) + "분 전";
        } else if (diffSeconds < 86400) { // 24시간 미만
            return ChronoUnit.HOURS.between(createdAt, now) + "시간 전";
        } else if (diffSeconds < 2592000) { // 30일 미만
            return ChronoUnit.DAYS.between(createdAt, now) + "일 전";
        } else if (diffSeconds < 31536000) { // 365일 미만
            return ChronoUnit.MONTHS.between(createdAt, now) + "개월 전";
        } else {
            return ChronoUnit.YEARS.between(createdAt, now) + "년 전";
        }
    }
}