package org.myan.web.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.List;

/**
 * Created by myan on 2017/8/14.
 * Intellij IDEA
 */
public class ProxyManager {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
        return (T) Enhancer.create(targetClass, (MethodInterceptor) (o, method, params, methodProxy) ->
                new ProxyChain(targetClass, o, method, methodProxy, params, proxyList).doProxyChain());
    }
}
