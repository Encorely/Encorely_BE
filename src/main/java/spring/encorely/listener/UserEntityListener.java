package spring.encorely.listener;

import jakarta.persistence.PostLoad;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.UserHandler;
import spring.encorely.domain.enums.Status;
import spring.encorely.domain.user.User;

public class UserEntityListener {

    @PostLoad
    public void filterInactiveUsers(User user) {
        if (!ListenerUtil.isListenerEnabled()) {
            return;
        }

        if (user.getStatus() == Status.INACTIVE) {
            throw new UserHandler(ErrorStatus.INACTIVE_USER);
        }
    }

}
