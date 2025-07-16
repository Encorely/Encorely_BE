package spring.encorely.apiPayload.exception.handler;

import spring.encorely.apiPayload.code.BaseErrorCode;
import spring.encorely.apiPayload.exception.GeneralException;

public class ReviewHandler extends GeneralException {

    public ReviewHandler(BaseErrorCode errorCode) { super(errorCode); }

}
