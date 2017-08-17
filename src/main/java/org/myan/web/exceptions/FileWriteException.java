package org.myan.web.exceptions;

/**
 * Created by myan on 2017/8/17.
 * Intellij IDEA
 */
public class FileWriteException extends RuntimeException {
    public FileWriteException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return ExceptionMessage.FILE_UPLOAD_ERROR.getMessage();
    }
}
