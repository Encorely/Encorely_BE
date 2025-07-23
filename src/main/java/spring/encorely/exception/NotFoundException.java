package spring.encorely.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus: 이 예외가 발생하면 HTTP 상태 코드 404 (Not Found)를 반환하도록 설정
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message); // RuntimeException의 생성자를 호출하여 메시지 전달
    }
}