package spring.encorely.dto.hallDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.encorely.domain.hall.Hall;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HallSearchResponseDto {
    private Long id;
    private String name;
    private String address;
    private String imageUrl;

    public static HallSearchResponseDto from(Hall hall) {
        return HallSearchResponseDto.builder()
                .id(hall.getId())
                .name(hall.getName())
                .address(hall.getAddress())
                .imageUrl(hall.getImageUrl())
                .build();
    }
}
