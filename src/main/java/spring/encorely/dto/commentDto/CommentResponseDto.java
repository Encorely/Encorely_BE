package spring.encorely.dto.commentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.encorely.domain.comment.Comment;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;
    private String content;
    private String userNickname;
    private String timeAgo;
    private boolean isMine; // 조회한 사용자가 해당 댓글을 작성했는지 알려줌
    private List<CommentResponseDto> children;

    public static CommentResponseDto fromEntity(Comment comment, Long currentUserId) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userNickname(comment.getUser().getNickname())
                .timeAgo(calculateTimeAgo(comment.getCreatedAt()))
                .isMine(comment.getUser().getId().equals(currentUserId))
                .children(mapChildren(comment, currentUserId))
                .build();
    }

    private static List<CommentResponseDto> mapChildren(Comment comment, Long currentUserId) {
        if (comment.getChildren() == null || comment.getChildren().isEmpty()) {
            return Collections.emptyList();
        }

        return comment.getChildren().stream()
                .map(child -> fromEntity(child, currentUserId))
                .collect(Collectors.toList());
    }

    private static String calculateTimeAgo(LocalDateTime createdAt) {
        if (createdAt == null) return "";

        LocalDateTime now = LocalDateTime.now();
        long seconds = ChronoUnit.SECONDS.between(createdAt, now);

        if (seconds < 60) return "방금 전";
        if (seconds < 3600) return ChronoUnit.MINUTES.between(createdAt, now) + "분 전";
        if (seconds < 86400) return ChronoUnit.HOURS.between(createdAt, now) + "시간 전";
        if (seconds < 2592000) return ChronoUnit.DAYS.between(createdAt, now) + "일 전";
        if (seconds < 31536000) return ChronoUnit.MONTHS.between(createdAt, now) + "개월 전";
        return ChronoUnit.YEARS.between(createdAt, now) + "년 전";
    }

}