package spring.encorely.apiPayload.exception.handler;

import spring.encorely.apiPayload.code.BaseErrorCode;
import spring.encorely.apiPayload.exception.GeneralException;

public class ScrapHandler extends GeneralException {
    public ScrapHandler(BaseErrorCode errorCode) { super(errorCode); }
}
