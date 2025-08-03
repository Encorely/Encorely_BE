package spring.encorely.dto.commentDto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {

        @Getter
        @Setter
        public static class CreateComment {

                @NotBlank(message = "댓글 내용은 필수입니다.")
                private String content;
                private Long parentId;

        }

}