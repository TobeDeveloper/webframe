package org.myan.web.beans;

import org.myan.web.helper.ClassHelper;
import org.myan.web.util.BeanInstanceUtil;

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
    }

    public Map<Class<?>, Object> getBeansMap() {
        return BEANS;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (!BEANS.containsKey(clazz))
            throw new RuntimeException("Could not find managed bean for class:" + clazz);
        return (T) BEANS.get(clazz);
    }
}
