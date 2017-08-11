package org.myan.web.exceptions;

/**
 * Created by myan on 2017/8/11.
 * Intellij IDEA
 */
public class InitializeException extends RuntimeException {

    public InitializeException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return ExceptionMessage.INIT_ERROR.getMessage();
    }
}
