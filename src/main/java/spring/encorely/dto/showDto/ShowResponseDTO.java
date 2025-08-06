package spring.encorely.dto.showDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ShowResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetOngoingShow {
        Long showId;
        String imageUrl;
        String showName;
        Long hallId;
        String hallName;
        LocalDate startDate;
        LocalDate endDate;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetShowDetail {
        String imageUrl;
        String showName;
        Long hallId;
        String hallName;
        LocalDate startDate;
        LocalDate endDate;
        Integer age;
        String link;
    }

}
