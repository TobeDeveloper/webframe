package org.myan.basic.aspect;

import org.myan.web.annotation.Aspect;
import org.myan.web.annotation.Controller;
import org.myan.web.proxy.AspectProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by myan on 2017/8/14.
 * Intellij IDEA
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerAspect.class);
    private long begin;

    @Override
    public void before(Class<?> clazz, Method method, Object[] params) throws Throwable {
        LOG.info("---------- controller method intercept begins ----------");
        LOG.info(String.format("# class: %s, method: %s \n", clazz.getName(), method.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> clazz, Method method, Object[] params) throws Throwable {
        LOG.info("# time cost: %s", System.currentTimeMillis() - begin);
        LOG.info("---------- controller method intercept ends ----------");
    }
}
