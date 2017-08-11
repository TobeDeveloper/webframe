package org.myan.web.mvc;

import org.myan.web.util.CastUtil;

import java.util.Map;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
public class Param {

    private Map<String, Object> params;

    public Param(Map<String, Object> params) {
        this.params = params;
    }

    public long getLong(String name) {
        return CastUtil.castLong(params.get(name));
    }

}
