package org.myan.web.exceptions;

/**
 * Created by myan on 2017/8/11.
 * Intellij IDEA
 */
public class ContextException extends RuntimeException {

    public ContextException(Throwable cause){
        super(cause);
    }

    public ContextException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return ExceptionMessage.CONTEXT_ERROR.getMessage();
    }
}
