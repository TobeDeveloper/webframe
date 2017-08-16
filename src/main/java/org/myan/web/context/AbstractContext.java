package org.myan.web.context;

import org.myan.web.annotation.Controller;
import org.myan.web.annotation.Service;
import org.myan.web.ConfigHelper;
import org.myan.web.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by myan on 2017/8/16.
 * Intellij IDEA
 */
public abstract class AbstractContext implements Context {
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getBasePackage();
        CLASS_SET = ClassUtil.loadClasses(basePackage);
    }

    static Set<Class<?>> getManagedClasses() {
        Set<Class<?>> managedClasses = new HashSet<>();
        managedClasses.addAll(getClassFromAnnotation(Service.class));
        managedClasses.addAll(getClassFromAnnotation(Controller.class));
        return managedClasses;
    }

    static Set<Class<?>> getClassFromSuper(Class<?> superClass) {
        Set<Class<?>> classes = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if(superClass.isAssignableFrom(clazz) && !superClass.equals(clazz))
                classes.add(clazz);
        }
        return classes;
    }

    static Set<Class<?>> getClassFromAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classes = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if(clazz.isAnnotationPresent(annotationClass))
                classes.add(clazz);
        }
        return classes;
    }

}
