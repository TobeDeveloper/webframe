package org.myan.web.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by myan on 2017/8/8.
 * Intellij IDEA
 */
public final class StringUtil {

    public static final String SEPARATOR = String.valueOf((char) 29);

    public static boolean isEmpty(String string) {
        if (string != null)
            string = string.trim();
        return StringUtils.isEmpty(string);
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static String[] splitString(String source, String delimer) {
        return source.split(delimer);
    }
}
