package org.myan.web;

import org.myan.web.util.PropertyLoader;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
public final class ConfigHelper {
    private static final PropertyLoader LOADER = PropertyLoader.getInstance(Constants.configFile);

    public static String getJdbcDriver() {
        return LOADER.getString(Constants.jdbcDriver);
    }

    public static String getJdbcUrl() {
        return LOADER.getString(Constants.jdbcUrl);
    }

    public static String getJdbcUser() {
        return LOADER.getString(Constants.jdbcUser);
    }

    public static String getJdbcPassword() {
        return LOADER.getString(Constants.jdbcPassword);
    }

    /* this should not be here.*/
    public static String getBasePackage() {
        return LOADER.getString(Constants.basePackage);
    }

    public static String getJspPath() {
        return LOADER.getString(Constants.jspPath, "/templates");
    }

    public static String getResourcePath() {
        return LOADER.getString(Constants.resourcePath, "/resources");
    }

    public static int getUploadLimit() {
        return LOADER.getInt(Constants.uploadLimit, 10);
    }
}
