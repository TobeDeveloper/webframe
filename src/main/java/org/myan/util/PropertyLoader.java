package org.myan.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by myan on 2017/8/8.
 * Intellij IDEA
 */
public final class PropertyLoader {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyLoader.class);

    public static Properties load(String fileName) {
        Properties prop = null;
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null)
                throw new FileNotFoundException(fileName + " not found.");

            prop = new Properties();
            prop.load(inputStream);
        } catch (IOException e) {
            LOG.error("Fail to load properties file.", e);
        }
        return prop;
    }

    public static String getString(Properties prop, String key) {
        return getString(prop, key, "");
    }

    public static String getString(Properties prop, String key, String defaultValue) {
        String value = defaultValue;
        if (prop.containsKey(key))
            value = prop.getProperty(key);
        return value;
    }

    public static int getInt(Properties prop, String key) {
        return getInt(prop, key, 0);
    }

    public static int getInt(Properties prop, String key, int defaultValue) {
        int value = defaultValue;
        if (prop.containsKey(key))
            value = CastUtil.castInt(prop.getProperty(key));
        return value;
    }

    public static boolean getBoolean(Properties prop, String key) {
        return getBoolean(prop, key, false);
    }

    public static boolean getBoolean(Properties prop, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (prop.containsKey(key))
            value = CastUtil.castBoolean(prop.getProperty(key));
        return value;
    }
}
