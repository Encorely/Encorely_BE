package spring.encorely.dto.hallDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class HallResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HallRankingList{
        List<HallRanking> hallRankingList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HallRanking{
        String name;
        String hallImageUrl;
        String address;
        Integer ranking;
    }
}