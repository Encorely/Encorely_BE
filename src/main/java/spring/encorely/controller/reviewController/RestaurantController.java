package spring.encorely.controller.reviewController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.domain.enums.RestaurantType;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.service.reviewService.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    @Operation(summary = "공연장별 맛집 후기 목록 조회 API")
    public ApiResponse<List<ReviewResponseDTO.GetRestaurant>> getRestaurants(@AuthenticationPrincipal UserDetails userDetails,
                                                                             @RequestParam Long hallId,
                                                                             @RequestParam(required = false) String keyword,
                                                                             @RequestParam(required = false) RestaurantType type,
                                                                             @RequestParam(defaultValue = "latest") String sort,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "20") int size) {
        String sortKey = ("popular").equalsIgnoreCase(sort) ? "popular" : "latest";
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        Long currentUserId = (userDetails != null) ? Long.parseLong(userDetails.getUsername()) : null;
        return ApiResponse.onSuccess(restaurantService.getRestaurants(currentUserId, hallId, keyword, type, sortKey, pageable));
    }

}
