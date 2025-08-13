package spring.encorely.dto.showDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.encorely.domain.show.Show;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowSearchResponseDto {

    private Long id;
    private String name;
    private String posterUrl;
    private String hallName;
    private String showDate;

    public static ShowSearchResponseDto from(Show show) {
        String showDate = show.getStartDate().toString().substring(0, 10) + "~" + show.getEndDate().toString().substring(8, 10);

        return ShowSearchResponseDto.builder()
                .id(show.getId())
                .name(show.getName())
                .posterUrl(show.getPosterUrl())
                .hallName(show.getHall().getName())
                .showDate(showDate)
                .build();
    }
}
