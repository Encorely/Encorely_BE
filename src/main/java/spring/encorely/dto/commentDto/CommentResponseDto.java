package spring.encorely.dto.commentDto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.encorely.domain.comment.Comment;

import java.time.LocalDateTime;
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

    @JsonBackReference("parent-children")
    private CommentResponseDto parent;

    @JsonManagedReference("parent-children")
    private List<CommentResponseDto> children;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.reviewId = comment.getReview() != null ? comment.getReview().getId() : null; // reviewId 추가
        this.userId = comment.getUser() != null ? comment.getUser().getId() : null; // userId 추가

        if (comment.getChildren() != null && !comment.getChildren().isEmpty()) {
            this.children = comment.getChildren().stream()
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        } else {
            this.children = null;
        }

        this.parent = null; // 초기화
        if (comment.getParent() != null) {

        }
    }
}
