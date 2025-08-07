package spring.encorely.service.addressService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class KakaoUriBuilderService {
    private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    public URI buildUriByAddressSearch(String address, int page, int size) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL)
                .queryParam("query", address)
                .queryParam("page", page)
                .queryParam("size", size);

        URI uri = uriBuilder.build().encode().toUri();

        return uri;
    }

}
