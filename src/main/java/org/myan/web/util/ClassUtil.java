package org.myan.web.util;

import org.myan.web.exceptions.InitializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by myan on 2017/8/9.
 * Intellij IDEA
 */
public final class ClassUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ClassUtil.class);

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /*load certain class*/
    public static Class<?> loadClass(String className, boolean initialize) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className, initialize, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOG.error("Failed to load class.", e);
            throw new InitializeException(e);
        }
        return clazz;
    }

    /*load classes under certain package*/
    public static Set<Class<?>> loadClasses(String packageName) {
        Set<Class<?>> classes = new HashSet<>();

        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        addClass(classes, packageName, packagePath);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
                        if (jarConnection != null) {
                            JarFile jarFile = jarConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while (jarEntries.hasMoreElements()) {
                                    JarEntry entry = jarEntries.nextElement();
                                    String entryName = entry.getName();
                                    if (entryName.endsWith(".class")) {
                                        String className = entryName.substring(0, entryName.lastIndexOf("."))
                                                .replaceAll("/", ".");
                                        addClass(classes, className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Failed to load classes.", e);
            throw new InitializeException(e);
        }
        return classes;
    }

    /*add single class*/
    private static void addClass(Set<Class<?>> classes, String className) {
        Class<?> clazz = loadClass(className, false);
        classes.add(clazz);
    }

    /*add package classes*/
    private static void addClass(Set<Class<?>> classes, String packageName, String packagePath) {
        File[] files = new File(packagePath).listFiles((f) -> (f.isFile() && f.getName().endsWith(".class"))
                || f.isDirectory());

        for (File file : files != null ? files : new File[0]) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtil.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                addClass(classes, className);
            } else {
                String subPackagePath = fileName;
                if (StringUtil.isNotEmpty(packagePath))
                    subPackagePath = packagePath + "/" + subPackagePath;
                String subPackageName = fileName;
                if (StringUtil.isNotEmpty(packageName))
                    subPackageName = packageName + "." + subPackageName;
                addClass(classes, subPackageName, subPackagePath);
            }
        }
    }
}
