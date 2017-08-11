package org.myan.web.helper;

import org.myan.web.annotation.Action;
import org.myan.web.annotation.HttpMethod;
import org.myan.web.beans.ControllerRequest;
import org.myan.web.beans.RequestHandler;
import org.myan.web.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
public final class ControllerHelper {
    private static final Map<ControllerRequest, RequestHandler> ACTION_MAP = new HashMap<>();

    static {
        Set<Class<?>> controllers = ClassHelper.getControllerClasses();
        if (CollectionUtil.isNotEmpty(controllers)) {
            for (Class<?> controller : controllers) {
                //get all method
                Method[] methods = controller.getDeclaredMethods();
                if (CollectionUtil.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        //set all actions
                        if (method.isAnnotationPresent(Action.class)) {
                            Action action = method.getAnnotation(Action.class);
                            HttpMethod[] httpMethod = action.method();
                            String path = action.path();
                            ControllerRequest request = new ControllerRequest(httpMethod, path);
                            RequestHandler handler = new RequestHandler(controller, method);
                            ACTION_MAP.put(request, handler);
                        }
                    }
                }
            }
        }
    }

    /*get a handler for certain controller request.*/
    public static RequestHandler getHandler(HttpMethod[] methods, String path) {
        ControllerRequest request = new ControllerRequest(methods, path);
        //FIXME there should add some validation.
        return ACTION_MAP.get(request);
    }


}
