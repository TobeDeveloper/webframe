package org.myan.util;

/**
 * Created by myan on 2017/8/8.
 * Intellij IDEA
 */
public final class CastUtil {

    public static String castString(Object property) {
        return castString(property, "");
    }

    public static String castString(Object property, String defaultValue) {
        return property == null ? String.valueOf(property) : defaultValue;
    }

    public static double castDouble(Object property) {
        return castDouble(property, 0);
    }

    public static double castDouble(Object property, double defaultValue) {
        double value = defaultValue;
        if (property != null) {
            String string = castString(property);
            if (StringUtil.isNotEmpty(string)) {
                try {
                    value = Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static long castLong(Object property) {
        return castLong(property, 0);
    }

    private static long castLong(Object property, long defaultValue) {
        long value = defaultValue;
        if (property != null) {
            String string = castString(property);
            if (StringUtil.isNotEmpty(string)) {
                try {
                    value = Long.parseLong(string);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static int castInt(Object property) {
        return castInt(property, 0);
    }

    private static int castInt(Object property, int defaultValue) {
        int value = defaultValue;
        if (property != null) {
            String string = castString(property);
            if (StringUtil.isNotEmpty(string)) {
                try {
                    value = Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static boolean castBoolean(String property) {
        return castBoolean(property, false);
    }

    private static boolean castBoolean(Object property, boolean defaultValue) {
        boolean value = defaultValue;
        if (property != null)
            value = Boolean.parseBoolean(castString(property));
        return value;
    }
}
