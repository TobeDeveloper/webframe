package org.myan.web.util;

import org.myan.web.exceptions.InitializeException;
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
    private static Properties prop;

    private PropertyLoader(String fileName) {
        load(fileName);
    }

    public static PropertyLoader getInstance(String fileName) {
        return new PropertyLoader(fileName);
    }

    private void load(String fileName) {
        Properties prop = null;
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null)
                throw new FileNotFoundException(fileName + " not found.");

            prop = new Properties();
            prop.load(inputStream);
        } catch (IOException e) {
            LOG.error("Fail to load properties file.", e);
            throw new InitializeException(e);
        }
        this.prop = prop;
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        String value = defaultValue;
        if (prop.containsKey(key))
            value = prop.getProperty(key);
        return value;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        int value = defaultValue;
        if (prop.containsKey(key))
            value = CastUtil.castInt(prop.getProperty(key));
        return value;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (prop.containsKey(key))
            value = CastUtil.castBoolean(prop.getProperty(key));
        return value;
    }
}
