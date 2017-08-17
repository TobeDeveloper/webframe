package org.myan.web.context;

import org.myan.web.annotation.Aspect;
import org.myan.web.annotation.Service;
import org.myan.web.proxy.AspectProxy;
import org.myan.web.proxy.Proxy;
import org.myan.web.proxy.ProxyManager;
import org.myan.web.proxy.TransactionProxy;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by myan on 2017/8/15.
 * Intellij IDEA
 */
public class AspectContext extends AbstractContext {

    private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
        Set<Class<?>> target = new HashSet<>();
        Class<? extends Annotation> annotation = aspect.value();
        if (!annotation.equals(Aspect.class))
            target.addAll(getClassFromAnnotation(annotation));
        return target;
    }

    /*
    * create a map to store the classes we want to proxy.
    * */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
        addAspectProxy(proxyMap);
        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        for (Class<?> proxyClass : getClassFromSuper(AspectProxy.class)) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClasses = createTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClasses);
            }
        }
    }

    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        proxyMap.put(TransactionProxy.class, getClassFromAnnotation(Service.class));
    }

    /*
    * set up all proxies to certain class.
    * */
    private static Map<Class<?>, List<Proxy>> proxyTarget(Map<Class<?>, Set<Class<?>>> proxyMap) {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();

        for (Map.Entry<Class<?>, Set<Class<?>>> entry : proxyMap.entrySet()) {
            Class<?> proxyClass = entry.getKey();
            Set<Class<?>> targetClasses = entry.getValue();
            for (Class<?> clazz : targetClasses) {
                try {
                    Proxy proxy = (Proxy) proxyClass.newInstance();
                    if (targetMap.containsKey(clazz))
                        targetMap.get(clazz).add(proxy);
                    else {
                        List<Proxy> proxyList = new ArrayList<>();
                        proxyList.add(proxy);
                        //replace certain injected bean with cglib proxy enhancer.
                        targetMap.put(clazz, proxyList);
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return targetMap;
    }

    static void init() {
        Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
        Map<Class<?>, List<Proxy>> proxyTarget = proxyTarget(proxyMap);

        for (Map.Entry<Class<?>, List<Proxy>> entry : proxyTarget.entrySet()) {
            Class<?> targetClass = entry.getKey();
            Object proxy = ProxyManager.createProxy(targetClass, entry.getValue());
            BeanContext.addBean(targetClass, proxy);
        }
    }
}
