package org.myan.web.context;

import org.myan.web.annotation.Action;
import org.myan.web.annotation.Controller;
import org.myan.web.annotation.HttpMethod;
import org.myan.web.beans.ControllerRequest;
import org.myan.web.beans.RequestHandler;
import org.myan.web.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by myan on 2017/8/16.
 * Hold all beans for web application
 */
public class WebApplicationContext extends AbstractContext {
    private static final Logger LOG = LoggerFactory.getLogger(WebApplicationContext.class);
    private static final Map<ControllerRequest, RequestHandler> ACTION_MAP = new HashMap<>();
    public static final RequestHandler NOT_FOUND_HANDLER = new RequestHandler();

    static {
        Set<Class<?>> controllers = getClassFromAnnotation(Controller.class);
        if (CollectionUtil.isNotEmpty(controllers)) {
            for (Class<?> controller : controllers) {
                Method[] methods = controller.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Action.class)) {
                        Action action = method.getAnnotation(Action.class);
                        HttpMethod[] httpMethod = action.method();
                        String path = action.path();
                        LOG.info(String.format("Mapped %s to %s.", path, method.getName()));
                        ControllerRequest request = new ControllerRequest(httpMethod, path);
                        RequestHandler handler = new RequestHandler(controller, method);
                        ACTION_MAP.put(request, handler);
                    }
                }
            }
        }
    }

    /*get a handler for certain controller request.*/
    public static RequestHandler getHandler(HttpMethod[] methods, String path) {
        ControllerRequest request = new ControllerRequest(methods, path);
        //FIXED there should add some validation.
        for (Map.Entry<ControllerRequest, RequestHandler> entry : ACTION_MAP.entrySet()) {
            HttpMethod[] allowedMethods = entry.getKey().getRequestMethod();
            if (entry.getKey().getRequestPath().equalsIgnoreCase(path)){
                if(Arrays.asList(allowedMethods).containsAll(Arrays.asList(methods)))
                    return ACTION_MAP.get(request);
                else
                    return NOT_FOUND_HANDLER;
            }
        }
        return null;
    }
}
