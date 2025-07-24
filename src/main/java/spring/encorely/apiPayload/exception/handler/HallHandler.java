package spring.encorely.apiPayload.exception.handler;

import spring.encorely.apiPayload.code.BaseErrorCode;
import spring.encorely.apiPayload.exception.GeneralException;

public class HallHandler extends GeneralException {

    public HallHandler(BaseErrorCode errorCode) { super(errorCode); }

}
