package org.myan.web.context;

import org.myan.web.annotation.Inject;
import org.myan.web.exceptions.ContextException;
import org.myan.web.util.CollectionUtil;
import org.myan.web.util.RefelectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
public class BeanContext extends AbstractContext {
    private static final Map<Class<?>, Object> BEANS = new HashMap<>();

    static {
        Set<Class<?>> classSet = getManagedClasses();
        for (Class<?> clazz : classSet) {
            BEANS.put(clazz, RefelectionUtil.newInstance(clazz));
        }
        //do apo init here.
        AspectContext.init();
        //we need to set the injected fields also.
        injectFields();
    }

    private static void injectFields() {
        if (CollectionUtil.isNotEmpty(BEANS)) {
            for (Map.Entry<Class<?>, Object> entry : BEANS.entrySet()) {
                Class<?> beanClass = entry.getKey();
                Object beanInstance = entry.getValue();
                Field[] beanFields = beanClass.getDeclaredFields();
                if (CollectionUtil.isNotEmpty(beanFields)) {
                    for (Field field : beanFields) {
                        if (field.isAnnotationPresent(Inject.class)) {
                            Class<?> fieldClass = field.getType();
                            Object fieldClassInstance = BEANS.get(fieldClass);
                            if (fieldClassInstance != null)
                                RefelectionUtil.setFieldValue(beanInstance, field, fieldClassInstance);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (!BEANS.containsKey(clazz))
            throw new ContextException("Could not find managed bean for class:" + clazz);
        return (T) BEANS.get(clazz);
    }

    public static void addBean(Class<?> clazz, Object object) {
        BEANS.put(clazz, object);
    }
}
