package org.myan.web.helper;

import org.myan.web.annotation.Controller;
import org.myan.web.annotation.Service;
import org.myan.web.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by myan on 2017/8/10.
 * load all managed classes
 */
public final class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getBasePackage();
        CLASS_SET = ClassUtil.loadClasses(basePackage);
    }

    /*load service classes*/
    public static Set<Class<?>> getServiceClasses() {
        Set<Class<?>> services = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Service.class))
                services.add(clazz);
        }
        return services;
    }

    public static Set<Class<?>> getControllerClasses() {
        Set<Class<?>> controllers = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Controller.class))
                controllers.add(clazz);
        }
        return controllers;
    }

    public static Set<Class<?>> getManagedClasses() {
        Set<Class<?>> managedClasses = new HashSet<>();
        managedClasses.addAll(getServiceClasses());
        managedClasses.addAll(getControllerClasses());
        return managedClasses;
    }
}
