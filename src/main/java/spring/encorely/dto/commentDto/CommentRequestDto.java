package spring.encorely.dto.commentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {

        @NotNull(message = "사용자 ID는 필수입니다.")
        private Long userId;

        @NotBlank(message = "댓글 내용은 필수입니다.")
        private String content;

        private Long parentId;
}