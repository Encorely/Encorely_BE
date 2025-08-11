package spring.encorely.controller.showController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.showDto.ShowResponseDTO;
import spring.encorely.dto.showDto.ShowSearchResponseDto;
import spring.encorely.service.showService.ShowService;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @GetMapping
    @Operation(summary = "현재 진행 중인 공연")
    public ApiResponse<List<ShowResponseDTO.GetOngoingShow>> getOngoingShows() {
        return ApiResponse.onSuccess(showService.getOngoingShows());
    }

    @GetMapping("/{showId}")
    @Operation(summary = "공연 상세 정보 불러오기")
    public ApiResponse<ShowResponseDTO.GetShowDetail> getOngoingShow(@PathVariable Long showId) {
        return ApiResponse.onSuccess(showService.getOngoingShow(showId));
    }

    @GetMapping("/ongoing/search")
    @Operation(summary = "현재 진행중인 공연 검색")
    public ApiResponse<List<ShowResponseDTO.GetOngoingShow>> searchOngoingShows(@RequestParam(required = false) String searchKeyword) {
        List<ShowResponseDTO.GetOngoingShow> showList = showService.searchShows(searchKeyword);
        return ApiResponse.onSuccess(showList);
    }

}
