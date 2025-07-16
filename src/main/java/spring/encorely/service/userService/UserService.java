package spring.encorely.service.userService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.UserHandler;
import spring.encorely.domain.user.User;
import spring.encorely.repository.userRepository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

}
