package org.myan.web.beans;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.myan.web.annotation.HttpMethod;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
public class ControllerRequest {
    private HttpMethod[] requestMethods;
    private String requestPath;

    public ControllerRequest(HttpMethod[] requestMethods, String requestPath) {
        this.requestMethods = requestMethods;
        this.requestPath = requestPath;
    }

    public HttpMethod[] getRequestMethod() {
        return requestMethods;
    }

    public void setRequestMethod(HttpMethod[] requestMethods) {
        this.requestMethods = requestMethods;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
