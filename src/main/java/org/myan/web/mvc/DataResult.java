package org.myan.web.mvc;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
public class DataResult {
    private Object dataModel;

    public DataResult(Object dataModel) {
        this.dataModel = dataModel;
    }

    public Object getDataModel() {
        return dataModel;
    }
}
