package org.mcs.Exception;

/**
 * created by SunHongbin on 2018/10/31
 */
public class CommonHttpException extends RuntimeException {

    public CommonHttpException(String message) {
        super(message);
    }

    public CommonHttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
