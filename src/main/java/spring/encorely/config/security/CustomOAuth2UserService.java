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
import spring.encorely.service.notificationService.NotificationService;
import spring.encorely.service.scrapService.ScrapFileService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final ScrapFileService scrapFileService;
    private final NotificationService notificationService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("[CustomOAuth2UserService] loadUser() called");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("[CustomOAuth2UserService] provider: {}", userRequest.getClientRegistration().getRegistrationId());
        log.info("[CustomOAuth2UserService] attributes from Kakao: {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = null;
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if ("kakao".equals(provider)) {
            Object idAttr = oAuth2User.getAttribute("id");
            if (idAttr != null) {
                providerId = idAttr.toString();
                log.info("[CustomOAuth2UserService] providerId: {}", providerId);
            } else {
                log.error("[CustomOAuth2UserService] Kakao user id attribute is null");
                throw new OAuth2AuthenticationException("Kakao user id attribute is null");
            }
        } else {
            log.error("[CustomOAuth2UserService] Unsupported provider: {}", provider);
            throw new OAuth2AuthenticationException("지원하지 않는 OAuth 제공자입니다.");
        }

        User user = userRepository.findByProviderAndProviderId(provider, providerId);
        if (user == null) {
            log.info("[CustomOAuth2UserService] New user, creating user in DB");
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
            scrapFileService.createDefaultFile(user);
            notificationService.createNotificationSettings(user);
        } else {
            log.info("[CustomOAuth2UserService] Existing user found in DB: {}", user.getId());
        }

        Map<String, Object> mappedAttributes = new HashMap<>(attributes);
        mappedAttributes.put("id", String.valueOf(user.getId()));
        log.info("[CustomOAuth2UserService] Returning CustomOAuth2User with id={}", user.getId());

        return new CustomOAuth2User(
                user.getId(),
                user.getRole(),
                mappedAttributes
        );
    }

}