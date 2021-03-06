package org.myan.web.util;

import org.myan.web.exceptions.ContextException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
public final class RefelectionUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RefelectionUtil.class);

    public static Object newInstance(Class<?> clazz) {
        Object instance = null;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Failed to create new instance", e);
            throw new ContextException(e);
        }
        return instance;
    }

    /*do invoke method staff*/
    public static Object invokeMethod(Object target, Method method, Object... params) {
        Object result;
        try {
            method.setAccessible(true);
            if (method.getParameterCount() == 0 || params.length == 0)
                result = method.invoke(target);
            else
                result = method.invoke(target, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOG.error("Failed to invoke method", e);
            throw new ContextException(e);
        }
        return result;
    }

    public static void setFieldValue(Object target, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            LOG.error("Failed to set field value", e);
            throw new ContextException(e);
        }
    }
}
