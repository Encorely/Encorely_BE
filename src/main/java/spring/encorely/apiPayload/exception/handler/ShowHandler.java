package spring.encorely.apiPayload.exception.handler;

import spring.encorely.apiPayload.code.BaseErrorCode;
import spring.encorely.apiPayload.exception.GeneralException;

public class ShowHandler extends GeneralException {

    public ShowHandler(BaseErrorCode errorCode) { super(errorCode); }

}