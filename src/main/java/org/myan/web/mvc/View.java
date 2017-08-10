package org.myan.web.mvc;

import org.myan.basic.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
public class View {
    private String path;

    private Map<String, Object> dataModels;

    public View(String path) {
        this.path = path;
        dataModels = new HashMap<>();
    }

    public void addModel(String key, Object value) {
        dataModels.put(key, value);
        //return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getDataModels() {
        return dataModels;
    }
}
