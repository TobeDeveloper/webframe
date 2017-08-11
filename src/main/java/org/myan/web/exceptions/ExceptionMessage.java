package org.myan.web.exceptions;

/**
 * Created by myan on 2017/8/11.
 * Intellij IDEA
 */
enum ExceptionMessage {
    INIT_ERROR("Failed to initialize the framework."),
    CONTEXT_ERROR("Failed to perform such operation."),
    CONVERT_ERROR("Failed to convert data objects.");

    private final String message;
    ExceptionMessage(String message){
        this.message = message;
    }

    String getMessage(){
        return message;
    }

}
