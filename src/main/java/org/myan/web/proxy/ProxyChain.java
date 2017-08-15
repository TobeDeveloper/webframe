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

    public Class<?> getTarget() {
        return target;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    private final Method targetMethod;
    private final MethodProxy methodProxy;

    public Object[] getParams() {
        return params;
    }

    private final Object[] params;

    private List<Proxy> proxies = new ArrayList<>();
    private int index = 0;

    public ProxyChain(Class<?> target, Object targetObject, Method targetMethod, MethodProxy methodProxy,
                      Object[] params, List<Proxy> proxyList) {
        this.target = target;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.params = params;
        this.proxies = proxyList;
    }

    public Object doProxyChain() throws Throwable{
        Object methodResult;
        if(index < proxies.size())
            methodResult = proxies.get(index++).proxy(this);
        else
            methodResult = methodProxy.invokeSuper(targetObject, params);
        return methodResult;
    }
}
