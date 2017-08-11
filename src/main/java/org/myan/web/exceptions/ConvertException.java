package org.myan.web.exceptions;

/**
 * Created by myan on 2017/8/11.
 * Intellij IDEA
 */
public class ConvertException extends RuntimeException {

    public ConvertException(Throwable cause){
        super(cause);
    }

    @Override
    public String getMessage() {
        return ExceptionMessage.CONVERT_ERROR.getMessage();
    }
}
