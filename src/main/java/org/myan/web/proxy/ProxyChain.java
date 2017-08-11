package org.myan.web.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by myan on 2017/8/11.
 * Intellij IDEA
 */
public class ProxyChain {

    private final Class<?> target;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] params;

    private List<Proxy> proxies = new ArrayList<>();
    private int index = 0;

    public ProxyChain(Class<?> target, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] params) {
        this.target = target;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.params = params;
    }

    public Object doProxyChain() throws Throwable{
        return null;
    }
}
