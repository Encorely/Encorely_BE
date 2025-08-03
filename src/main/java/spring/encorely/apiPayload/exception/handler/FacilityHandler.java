package spring.encorely.apiPayload.exception.handler;

import spring.encorely.apiPayload.code.BaseErrorCode;
import spring.encorely.apiPayload.exception.GeneralException;

public class FacilityHandler extends GeneralException {

    public FacilityHandler(BaseErrorCode errorCode) { super(errorCode); }

}