package spring.encorely.dto.tokenDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class TokenRequestDTO {

    @Getter
    @Setter
    public static class GetToken {

        @NotBlank
        private String refreshToken;

    }

    @Getter
    @Setter
    public static class Logout {

        @NotBlank
        private String accessToken;
        @NotBlank
        private String refreshToken;
    }

    @Getter
    @Setter
    public static class DeleteUser {

        @NotBlank
        private String accessToken;
        @NotBlank
        private String refreshToken;
    }

}
