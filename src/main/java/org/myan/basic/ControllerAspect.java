package org.myan.basic;

import org.myan.web.annotation.Aspect;
import org.myan.web.annotation.Controller;
import org.myan.web.proxy.AspectProxy;

import java.lang.reflect.Method;

/**
 * Created by myan on 2017/8/14.
 * Intellij IDEA
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {
    private long begin;

    @Override
    public void before(Class<?> clazz, Method method, Object[] params) throws Throwable {
        super.before(clazz, method, params);
    }

    @Override
    public void after(Class<?> clazz, Method method, Object[] params) throws Throwable {
        super.after(clazz, method, params);
    }
}
