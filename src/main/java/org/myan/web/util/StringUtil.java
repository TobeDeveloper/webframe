package org.myan.web.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by myan on 2017/8/8.
 * Intellij IDEA
 */
public final class StringUtil {

    public static boolean isEmpty(String string) {
        if (string != null)
            string = string.trim();
        return StringUtils.isEmpty(string);
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }
}
