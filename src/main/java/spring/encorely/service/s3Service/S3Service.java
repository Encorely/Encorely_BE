package spring.encorely.service.s3Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.encorely.dto.s3Dto.S3ResponseDTO;

import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${custom.s3.bucket}")
    private String bucket;

    @Transactional
    public S3ResponseDTO generatePresignedUrl (String fileName, String contentType) {
        String key = "dev/" + UUID.randomUUID() + "_" + fileName;

        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 5);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);
        request.setContentType(contentType);

        URL url = s3Client.generatePresignedUrl(request);

        return new S3ResponseDTO(url.toString(), key);
    }

    public String getPublicUrl(String key) {
        return s3Client.getUrl(bucket, key).toString();
    }

    public void delete(String imageUrl) {
        URI uri = URI.create(imageUrl);
        String key = uri.getPath().substring(1);
        s3Client.deleteObject(bucket, key);
    }

}
