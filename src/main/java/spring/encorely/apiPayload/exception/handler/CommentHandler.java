package spring.encorely.apiPayload.exception.handler;

import spring.encorely.apiPayload.code.BaseErrorCode;
import spring.encorely.apiPayload.exception.GeneralException;

public class CommentHandler extends GeneralException {
  public CommentHandler(BaseErrorCode errorCode) { super(errorCode); }

}
