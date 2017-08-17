package org.myan.web.mvc;

import org.myan.web.util.CodecUtil;
import org.myan.web.util.CollectionUtil;
import org.myan.web.util.StreamUtil;
import org.myan.web.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by myan on 2017/8/17.
 * Intellij IDEA
 */
class RequestParamCreator {

    static Param createParam(HttpServletRequest req) throws IOException {
        List<FormParam> formParams = new ArrayList<>();
        formParams.addAll(parseParameterNames(req));
        formParams.addAll(parseInputStream(req));
        return new Param(formParams);
    }

    //add all form parameters
    private static List<FormParam> parseInputStream(HttpServletRequest req) {
        List<FormParam> params = new ArrayList<>();
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String fieldName = paramNames.nextElement();
            String[] fieldValues = req.getParameterValues(fieldName);
            if (CollectionUtil.isNotEmpty(fieldValues)) {
                Object fieldValue;
                if (fieldValues.length == 1)
                    fieldValue = fieldValues[0];
                else {
                    StringBuilder builder = new StringBuilder("");
                    for (int i = 0; i < fieldValues.length; i++) {
                        builder.append(fieldValues[i]);
                        if (i != fieldValues.length - 1)
                            builder.append(StringUtil.SEPARATOR);
                    }
                    fieldValue = builder.toString();
                }
                params.add(new FormParam(fieldName, fieldValue));
            }
        }

        return params;
    }

    //add all url carried parameters
    private static Collection<? extends FormParam> parseParameterNames(HttpServletRequest req) throws IOException {
        List<FormParam> params = new ArrayList<>();
        String requestBody = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
        if (StringUtil.isNotEmpty(requestBody)) {
            String[] kvs = StringUtil.splitString(requestBody, "&");
            if (CollectionUtil.isNotEmpty(params)) {
                for (String kv : kvs) {
                    String[] array = StringUtil.splitString(kv, "=");
                    if (CollectionUtil.isNotEmpty(array) && array.length == 2) {
                        params.add(new FormParam(array[0], array[1]));
                    }
                }
            }
        }


        return params;
    }
}
