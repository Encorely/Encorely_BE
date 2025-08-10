package spring.encorely.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spring.encorely.apiPayload.code.status.ErrorStatus;
import spring.encorely.apiPayload.exception.handler.NotificationHandler;
import spring.encorely.domain.enums.NotificationType;
import spring.encorely.domain.review.Review;
import spring.encorely.domain.user.User;

@Component
@RequiredArgsConstructor
public class NotificationMessageFactory {

    public String createMessage(NotificationType type, User sender, Review review, String message) {
        switch (type) {
            case COMMENT:
                return sender.getNickname() + "님이 댓글을 남겼어요.";

            case SCRAP:
                return sender.getNickname() + "님이 회원님의 글을 스크랩했어요.";

            case LIKE:
                return sender.getNickname() + "님이 회원님의 글을 좋아합니다.";

            default:
                return message;
        }
    }

}
