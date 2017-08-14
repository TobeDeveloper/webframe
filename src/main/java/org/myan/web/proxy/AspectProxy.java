package org.myan.web.proxy;

import java.lang.reflect.Method;

/**
 * Created by myan on 2017/8/14.
 * Intellij IDEA
 */
public abstract class AspectProxy implements Proxy {

    @Override
    public Object proxy(ProxyChain chain) throws Throwable {
        return null;
    }

    public void begin() {

    }

    public boolean intercept(Class<?> clazz, Method method, Object[] params) throws Throwable{
        return true;
    }

    public void before(Class<?> clazz, Method method, Object[] params) throws Throwable {

    }

    public void after(Class<?> clazz, Method method, Object[] params) throws Throwable {

    }

    public void error(Class<?> clazz, Method method, Object[] params) throws Throwable {

    }
}
