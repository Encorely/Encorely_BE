package spring.encorely.controller.addressController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.encorely.apiPayload.ApiResponse;
import spring.encorely.dto.kakaoLocalDto.KakaoLocalResponseDTO;
import spring.encorely.service.addressService.AddressService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping()
    @Operation(summary = "주소 검색 API")
    public ApiResponse<KakaoLocalResponseDTO.AddressListDTO> addressSearch(@RequestParam String keyword,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(addressService.addressSearch(keyword, page, size));
    }
}
