package org.myan.web.beans;

import org.myan.web.annotation.Aspect;
import org.myan.web.helper.ClassHelper;
import org.myan.web.proxy.AspectProxy;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by myan on 2017/8/15.
 * Intellij IDEA
 */
public class AspectContext {

    static {
        init();
    }

    private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
        Set<Class<?>> target = new HashSet<>();
        Class<? extends Annotation> annotation = aspect.value();
        if(!annotation.equals(Aspect.class))
            target.addAll(ClassHelper.getClassFromAnnotation(annotation));
        return target;
    }

    private static Map<Class<?>, Set<Class<?>>> createProxyMap() {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
        for (Class<?> proxyClass : ClassHelper.getClassFromSuper(AspectProxy.class)) {
            if(proxyClass.isAnnotationPresent(Aspect.class)){
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClasses = createTargetClassSet(aspect);
                proxyMap.put(proxyClass, targetClasses);
            }
        }
        return proxyMap;
    }


    static void init(){

    }
}
