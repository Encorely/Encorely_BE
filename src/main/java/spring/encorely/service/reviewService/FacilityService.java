package spring.encorely.service.reviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.domain.review.Facility;
import spring.encorely.domain.review.Review;
import spring.encorely.dto.reviewDto.ReviewRequestDTO;
import spring.encorely.repository.reviewRepository.FacilityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final ReviewImageService reviewImageService;

    @Transactional
    public void saveFacility(Review review, List<ReviewRequestDTO.FacilityInfo> infos) {
        for (ReviewRequestDTO.FacilityInfo info : infos) {
            Facility facility = Facility.builder()
                    .review(review)
                    .name(info.getName())
                    .facilityType(info.getFacilityType())
                    .tips(info.getTips())
                    .address(info.getAddress())
                    .latitude(info.getLatitude())
                    .longitude(info.getLongitude())
                    .build();

            facilityRepository.save(facility);
            reviewImageService.saveFacilityImages(facility, info.getImageUrls());
        }
    }

}
