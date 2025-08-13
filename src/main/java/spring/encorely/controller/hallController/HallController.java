package spring.encorely.controller.hallController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.hallDto.HallResponseDTO;
import spring.encorely.dto.hallDto.HallSearchResponseDto;
import spring.encorely.service.hallService.HallService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/halls")
public class HallController {

    private final HallService hallService;

    @GetMapping()
    @Operation(summary = "공연장 리스트")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
            @Parameter(name = "size", description = "페이지 크기", example = "10"),
            @Parameter(name = "sort", description = "정렬 기준 필드, 예: ranking", example = "ranking")
    })
    public ApiResponse<HallResponseDTO.HallRankingList> getHallList(@PageableDefault(size = 10, sort = "ranking") Pageable pageable) {
        return ApiResponse.onSuccess(hallService.getHallList(pageable));
    }

    @GetMapping("/ranking")
    @Operation(summary = "인기 있는 공연장 상위 6개")
    public ApiResponse<HallResponseDTO.HallRankingList> getHallRankingList() {
        return ApiResponse.onSuccess(hallService.getHallRankingList());
    }

    @GetMapping("/search")
    @Operation(summary = "공연장 검색")
    public ApiResponse<List<HallSearchResponseDto>> searchHalls(@RequestParam(required = false) String searchKeyword) {
        List<HallSearchResponseDto> hallList = hallService.searchHalls(searchKeyword);
        return ApiResponse.onSuccess(hallList);
    }
}
