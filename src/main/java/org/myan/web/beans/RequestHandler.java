package org.myan.web.beans;

import java.lang.reflect.Method;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
public class RequestHandler {
    private Class<?> controllerClass;
    private Method requestMethod;

    public RequestHandler() { }

    public RequestHandler(Class<?> controllerClass, Method requestMethod) {
        this.controllerClass = controllerClass;
        this.requestMethod = requestMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(Method requestMethod) {
        this.requestMethod = requestMethod;
    }
}
