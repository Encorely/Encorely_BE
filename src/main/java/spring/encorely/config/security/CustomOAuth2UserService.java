package spring.encorely.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import spring.encorely.domain.enums.Role;
import spring.encorely.domain.enums.Status;
import spring.encorely.domain.user.User;
import spring.encorely.repository.userRepository.UserRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        String providerId = null;
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if ("kakao".equals(provider)) {
            Object idAttr = oAuth2User.getAttribute("id");
            if (idAttr != null) {
                providerId = idAttr.toString();
            } else {
                throw new OAuth2AuthenticationException("Kakao user id attribute is null");
            }
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 OAuth 제공자입니다.");
        }

        User user = userRepository.findByProviderAndProviderId(provider, providerId);
        if (user == null) {
            user = userRepository.save(
                    User.builder()
                            .provider(provider)
                            .providerId(providerId)
                            .followers(0)
                            .followings(0)
                            .historyCount(0)
                            .role(Role.USER)
                            .status(Status.ACTIVE)
                            .build()
            );
        }
        Map<String, Object> mappedAttributes = new HashMap<>(attributes);
        mappedAttributes.put("id", String.valueOf(user.getId()));

        return new CustomOAuth2User(
                user.getId(),
                user.getRole(),
                mappedAttributes
        );
    }

}