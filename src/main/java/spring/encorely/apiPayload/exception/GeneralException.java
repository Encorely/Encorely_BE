package spring.encorely.apiPayload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import spring.encorely.apiPayload.code.BaseErrorCode;
import spring.encorely.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorHttpStatus() {
        return this.code.getReasonHttpStatus();
    }

}