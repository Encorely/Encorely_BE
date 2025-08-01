package spring.encorely.dto.userDto;

import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

    @Getter
    @Setter
    public static class UpdateUser {

        String imageUrl;
        String nickname;
        String introduction;
        String link;

    }

}
