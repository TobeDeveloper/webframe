package org.myan.web.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by myan on 2017/8/11.
 * Intellij IDEA
 */
public class CGlibProxy implements MethodInterceptor {
    private static CGlibProxy instance = new CGlibProxy();

    private CGlibProxy() {
    }

    public CGlibProxy getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> target) {
        return (T) Enhancer.create(target, this);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        //do some aop here.
        Object result = proxy.invokeSuper(o, args);
        return result;
    }
}
