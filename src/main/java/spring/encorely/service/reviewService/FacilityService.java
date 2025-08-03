package spring.encorely.service.reviewService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.FacilityHandler;
import spring.encorely.domain.review.Facility;
import spring.encorely.domain.review.Restaurant;
import spring.encorely.domain.review.Review;
import spring.encorely.dto.reviewDto.ReviewRequestDTO;
import spring.encorely.dto.reviewDto.ReviewResponseDTO;
import spring.encorely.repository.reviewRepository.FacilityRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final ReviewImageService reviewImageService;

    public Facility findById(Long id) {
        return facilityRepository.findById(id).orElseThrow(() -> new FacilityHandler(ErrorStatus.FACILITY_NOT_FOUND));
    }

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
            reviewImageService.saveFacilityImage(facility, info.getImageUrl());
        }
    }

    public List<ReviewResponseDTO.Facility> getFacilities(Review review) {
        List<ReviewResponseDTO.Facility> dtos = new ArrayList<>();
        List<Facility> facilities = facilityRepository.findAllByReview(review);

        for (Facility facility : facilities) {
            ReviewResponseDTO.Facility dto = ReviewResponseDTO.Facility.builder()
                    .facilityId(facility.getId())
                    .facilityType(facility.getFacilityType())
                    .image(reviewImageService.getImage(null, facility))
                    .facilityName(facility.getName())
                    .address(facility.getAddress())
                    .latitude(facility.getLatitude())
                    .longitude(facility.getLongitude())
                    .tips(facility.getTips())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    public void updateFacilities(List<ReviewRequestDTO.FacilityUpdateInfo> infos) {
        for (ReviewRequestDTO.FacilityUpdateInfo info : infos) {
            Facility facility = findById(info.getFacilityId());

            if (info.getFacilityType() != null) { facility.setFacilityType(info.getFacilityType()); }

            if (info.getName() != null) { facility.setName(info.getName()); }

            if (info.getAddress() != null) { facility.setAddress(info.getAddress()); }

            if (info.getLatitude() != null) { facility.setLatitude(info.getLatitude()); }

            if (info.getLongitude() != null) { facility.setLongitude(info.getLongitude()); }

            if (info.getTips() != null) { facility.setTips(info.getTips()); }

            if (info.getImageUrl() != null) { reviewImageService.saveFacilityImage(facility, info.getImageUrl()); }

        }
    }

}
