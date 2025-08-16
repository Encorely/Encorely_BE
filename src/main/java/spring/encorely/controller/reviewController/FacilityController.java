package spring.encorely.controller.reviewController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.domain.enums.FacilityType;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.service.reviewService.FacilityService;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping
    @Operation(summary = "공연장별 편의시설 후기 목록 조회 API")
    public ApiResponse<List<ReviewResponseDTO.GetFacility>> getFacilities(@RequestParam Long hallId,
                                                                          @RequestParam(required = false) String keyword,
                                                                          @RequestParam(required = false) FacilityType type,
                                                                          @RequestParam(defaultValue = "latest") String sort,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "20") int size) {
        String sortKey = ("popular".equalsIgnoreCase(sort)) ? "popular" : "latest";
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        return ApiResponse.onSuccess(facilityService.getFacilities(hallId, keyword, type, sortKey, pageable));
    }

}
