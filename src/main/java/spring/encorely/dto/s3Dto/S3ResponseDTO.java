package spring.encorely.dto.s3Dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class S3ResponseDTO {

    String url;
    String key;

}
