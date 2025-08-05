package spring.encorely.service.scrapService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.ScrapHandler;
import spring.encorely.domain.enums.NotificationType;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.scrap.ScrapFile;
import spring.encorely.domain.scrap.ScrapReview;
import spring.encorely.domain.user.User;
import spring.encorely.dto.scrapDto.ScrapFileRequestDTO;
import spring.encorely.dto.scrapDto.ScrapFileResponseDTO;
import spring.encorely.repository.scrapRepository.ScrapFileRepository;
import spring.encorely.repository.scrapRepository.ScrapReviewRepository;
import spring.encorely.service.notificationService.NotificationService;
import spring.encorely.service.reviewService.ReviewService;
import spring.encorely.service.userService.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScrapFileService {
    private final ScrapReviewRepository scrapReviewRespository;
    private final ScrapFileRepository scrapFileRespository;
    private final ReviewService reviewService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Transactional
    public ScrapFileResponseDTO.addReviewToFile toggleScrapReview(Long userId, ScrapFileRequestDTO.addReviewToFile request) {
        User user = userService.findById(userId);
        Review review = reviewService.findById(request.getReviewId());

        ScrapFile defaultFile = scrapFileRespository.findByUserAndIsDefault(user, true)
                .orElseThrow(() -> new ScrapHandler(ErrorStatus.DEFAULT_SCRAP_FILE_NOT_FOUND));

        if (request.isScrapped()) {
            scrapReviewRespository.deleteByReviewAndScrapFile_User(review, user);
            return new ScrapFileResponseDTO.addReviewToFile(null, LocalDateTime.now());
        } else {
            ScrapReview scrapReview = ScrapReview.builder()
                    .scrapFile(defaultFile)
                    .review(review)
                    .category(request.getCategory())
                    .build();

            scrapReviewRespository.save(scrapReview);
            notificationService.createNotification(user, review, NotificationType.SCRAP, null);

            return new ScrapFileResponseDTO.addReviewToFile(scrapReview.getId(), scrapReview.getCreatedAt());
        }
    }

    @Transactional
    public void createDefaultFile(User user) {
        ScrapFile scrapFile = ScrapFile.builder()
                .user(user)
                .name("기본폴더")
                .isDefault(true)
                .build();

        scrapFileRespository.save(scrapFile);
    }

    @Transactional
    public ScrapFileResponseDTO.addFile addFile(Long userId) {
        User user = userService.findById(userId);

        int suffix = 1;
        String baseName = "File";
        String generatedName = baseName + suffix;

        while (scrapFileRespository.existsByUserAndName(user, generatedName)) {
            suffix++;
            generatedName = baseName + suffix;
        }

        ScrapFile scrapFile = ScrapFile.builder()
                .user(user)
                .name(generatedName)
                .build();

        scrapFileRespository.save(scrapFile);
        return new ScrapFileResponseDTO.addFile(scrapFile.getId(), scrapFile.getName(), scrapFile.getCreatedAt());
    }

    @Transactional
    public ScrapFileResponseDTO.updateFileName updateFileName(Long userId, Long fileId, ScrapFileRequestDTO.updateFileName request) {
        User user = userService.findById(userId);
        ScrapFile scrapFile = scrapFileRespository.findById(fileId).orElseThrow(() -> new ScrapHandler(ErrorStatus.SCRAP_FILE_NOT_FOUND));

        if(scrapFileRespository.existsByUserAndName(user, request.getName())) {
            throw new ScrapHandler(ErrorStatus.SCRAP_FILE_NAME_DUPLICATION);
        }

        scrapFile.changeName(request.getName());
        scrapFileRespository.save(scrapFile);

        return new ScrapFileResponseDTO.updateFileName(scrapFile.getId(), scrapFile.getName(), scrapFile.getUpdatedAt());
    }

    @Transactional
    public void moveScrapReviewToAnotherFolder(Long userId, ScrapFileRequestDTO.MoveReview request) {
        User user = userService.findById(userId);
        ScrapReview scrapReview = scrapReviewRespository.findById(request.getScrapReviewId())
                .orElseThrow(() -> new ScrapHandler(ErrorStatus.SCRAP_REVIEW_NOT_FOUND));

        ScrapFile scrapFile = scrapFileRespository.findById(request.getTargetScrapFileId())
                .orElseThrow(() -> new ScrapHandler(ErrorStatus.SCRAP_FILE_NOT_FOUND));

        scrapReview.setScrapFile(scrapFile);
    }

    public void deleteFile(Long fileId) {
        scrapFileRespository.deleteById(fileId);
    }

    public ScrapFileResponseDTO.GetFile getFiles(Long userId) {
        User user = userService.findById(userId);
        List<ScrapFile> files = scrapFileRespository.findAllByUser(user);
        List<ScrapFileResponseDTO.FileInfo> dtos = new ArrayList<>();

        for (ScrapFile file : files) {
            List<ScrapReview> scrapReviews = scrapReviewRespository.findAllByScrapFile(file);

            String imageUrl = scrapReviews.stream()
                    .sorted(Comparator.comparing(ScrapReview::getCreatedAt))
                    .map(scrapReview -> {
                        Review review = scrapReview.getReview();
                        return review.getReviewImageList().isEmpty() ? null : review.getReviewImageList().get(0).getImageUrl();
                    })
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

            ScrapFileResponseDTO.FileInfo dto = ScrapFileResponseDTO.FileInfo.builder()
                    .fileId(file.getId())
                    .name(file.getName())
                    .imageUrl(imageUrl)
                    .build();

            dtos.add(dto);
        }

        ScrapFileResponseDTO.GetFile result = ScrapFileResponseDTO.GetFile.builder()
                .numOfFiles(files.size())
                .files(dtos)
                .build();
        return result;
    }

}
