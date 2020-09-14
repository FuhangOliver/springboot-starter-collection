package com.ciwei.smsserver.exception;

/**
 *功能描述
 * @author FuHang
 * @date 2019/8/5
 */
public class SmsException extends RuntimeException {
    public SmsException() {
        super();
    }

    public SmsException(String message) {
        super(message);
    }

    public SmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmsException(Throwable cause) {
        super(cause);
    }

}
