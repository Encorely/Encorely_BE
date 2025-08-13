package spring.encorely.dto.kakaoLocalDto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class KakaoLocalResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDTO {
        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("y")
        private double latitude;

        @JsonProperty("x")
        private double longitude;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MetaDto {

        @JsonProperty("total_count")
        private Integer totalCount;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressListDTO {
        @JsonProperty("meta")
        private MetaDto metaDto;

        @JsonProperty("documents")
        List<AddressDTO> addressList;
    }
}
