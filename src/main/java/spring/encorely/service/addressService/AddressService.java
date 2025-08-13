package spring.encorely.service.addressService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import spring.encorely.dto.kakaoLocalDto.KakaoLocalResponseDTO;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final RestTemplate restTemplate;
    private final KakaoUriBuilderService kakaoUriBuilderService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoRestApiKey;

    public KakaoLocalResponseDTO.AddressListDTO addressSearch(String address, int page, int size) {
        if(ObjectUtils.isEmpty(address)) return null;

        URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address, page, size);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity(headers);

        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoLocalResponseDTO.AddressListDTO.class).getBody();
    }
}
