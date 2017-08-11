package org.myan.web.beans;

import org.myan.web.annotation.Inject;
import org.myan.web.helper.ClassHelper;
import org.myan.web.util.BeanInstanceUtil;
import org.myan.web.util.CollectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
public class BeanContext {
    private static final Map<Class<?>, Object> BEANS = new HashMap<>();

    static {
        Set<Class<?>> classSet = ClassHelper.getManagedClasses();
        for (Class<?> clazz : classSet) {
            BEANS.put(clazz, BeanInstanceUtil.newInstance(clazz));
        }
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
                        if(field.isAnnotationPresent(Inject.class)){
                            Class<?> fieldClass = field.getType();
                            Object fieldClassInstance = BEANS.get(fieldClass);
                            if(fieldClassInstance != null)
                                BeanInstanceUtil.setFieldValue(beanInstance, field, fieldClassInstance);
                        }
                    }
                }
            }
        }
    }

    /*BEANS should not exploded to other class.
    public Map<Class<?>, Object> getBeansMap() {
        return BEANS;
    }
    */

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (!BEANS.containsKey(clazz))
            throw new RuntimeException("Could not find managed bean for class:" + clazz);
        return (T) BEANS.get(clazz);
    }
}
