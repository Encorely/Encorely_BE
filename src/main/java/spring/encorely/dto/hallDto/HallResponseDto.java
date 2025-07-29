package spring.encorely.dto.hallDto;

import lombok.Getter;
import spring.encorely.domain.hall.Hall;

import java.time.LocalDateTime;

@Getter
public class HallResponseDto {
    private Long id;
    private String name;
    private String address;
    private String imageUrl;
    private Integer clickCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HallResponseDto(Hall hall) {
        this.id = hall.getId();
        this.name = hall.getName();
        this.address = hall.getAddress();
        this.imageUrl = hall.getImageUrl();
        this.clickCount = hall.getClickCount();
        this.createdAt = hall.getCreatedAt();
        this.updatedAt = hall.getUpdatedAt();
    }
}