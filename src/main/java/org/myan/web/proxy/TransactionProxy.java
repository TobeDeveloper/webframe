package org.myan.web.proxy;

import org.myan.web.annotation.Transactional;
import org.myan.web.util.DBUtil;

import java.lang.reflect.Method;

/**
 * Created by myan on 2017/8/17.
 * Intellij IDEA
 */
public class TransactionProxy implements Proxy {
    private static final ThreadLocal<Boolean> FLAG_HOLDER = ThreadLocal.withInitial(() -> false);

    @Override
    public Object proxy(ProxyChain chain) throws Throwable {
        Object result;
        Boolean flag = FLAG_HOLDER.get();
        Method method = chain.getTargetMethod();
        if (!flag && method.isAnnotationPresent(Transactional.class)) {
            FLAG_HOLDER.set(true);

            try {
                DBUtil.beginTransaction();
                result = chain.doProxyChain();
                DBUtil.commitTransaction();
            } catch (Exception e) {
                DBUtil.rollbackTransaction();
                throw e;
            } finally {
                FLAG_HOLDER.remove();
            }
        } else {
            result = chain.doProxyChain();
        }
        return result;
    }

}
