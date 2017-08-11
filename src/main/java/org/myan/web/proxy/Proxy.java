package org.myan.web.proxy;

/**
 * Created by myan on 2017/8/11.
 * Intellij IDEA
 */
public interface Proxy {
    Object proxy(ProxyChain chain) throws Throwable;
}
