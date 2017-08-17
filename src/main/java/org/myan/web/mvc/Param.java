package org.myan.web.mvc;

import org.myan.web.util.CastUtil;
import org.myan.web.util.CollectionUtil;
import org.myan.web.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
public class Param {

    private List<FormParam> formParams;
    private List<FileParam> fileParams;

    public Param(List<FormParam> formParams) {
        this.formParams = formParams;
    }

    public Param(List<FormParam> formParams, List<FileParam> fileParams) {
        this.formParams = formParams;
        this.fileParams = fileParams;
    }

    public Map<String, Object> getFieldMap() {
        Map<String, Object> fieldMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(formParams)) {
            for (FormParam param : formParams) {
                String fieldName = param.getFieldName();
                Object fieldValue = param.getFieldValue();
                if (fieldMap.containsKey(fieldName))
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
                fieldMap.put(fieldName, fieldValue);
            }
        }

        return fieldMap;
    }

    /* get all file fields*/
    public Map<String, List<FileParam>> getFileMap() {
        Map<String, List<FileParam>> fileMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(fileParams)) {
            for (FileParam param : fileParams) {
                String fieldName = param.getFieldName();
                List<FileParam> fileParamList;
                if (fileMap.containsKey(fieldName))
                    fileParamList = fileMap.get(fieldName);
                else
                    fileParamList = new ArrayList<>();
                fileParamList.add(param);
                fileMap.put(fieldName, fileParamList);
            }
        }

        return fileMap;
    }

    public List<FileParam> getFileList(String fieldName) {
        return getFileMap().get(fieldName);
    }

    public FileParam getFile(String fieldName) {
        List<FileParam> fileParamList = getFileList(fieldName);
        if (CollectionUtil.isNotEmpty(fileParamList) && fileParamList.size() == 1)
            return fileParamList.get(0);
        return null;
    }

    public String getString(String fieldName) {
        return CastUtil.castString(getFieldMap().get(fieldName));
    }

    public boolean getBoolean(String fieldName) {
        return CastUtil.castBoolean(getFieldMap().get(fieldName));
    }

    public double getDouble(String fieldName) {
        return CastUtil.castDouble(getFieldMap().get(fieldName));
    }

    public int getInt(String fieldName) {
        return CastUtil.castInt(getFieldMap().get(fieldName));
    }

    public long getLong(String fieldName) {
        return CastUtil.castLong(getFieldMap().get(fieldName));
    }

}
