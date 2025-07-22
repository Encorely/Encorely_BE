package spring.encorely.dto.testDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestResponseDTO {

    String accessToken;
    String refreshToken;

}
