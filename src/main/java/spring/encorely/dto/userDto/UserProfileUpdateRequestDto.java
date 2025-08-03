package spring.encorely.dto.userDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateRequestDto {
    private String nickname;
    private String introduction;
    private String link;
}
