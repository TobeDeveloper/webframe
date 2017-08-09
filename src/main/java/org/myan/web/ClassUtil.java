package org.myan.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /*load certain class*/
    public static Class<?> loadClass(String className, boolean initialize){
        Class<?> clazz;
        try {
            clazz = Class.forName(className, initialize, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOG.error("Failed to load class.", e);
            throw new RuntimeException(e);
        }
        return clazz;
    }

    /*load classes under certain package*/
    public static Set<Class<?>> loadClasses(String packageName){
        Set<Class<?>> classes = new HashSet<>();

        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url != null) {
                    String protocol = url.getProtocol();
                    if(protocol.equals("file")){
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        addClass(classes, packageName, packagePath);
                    }else if (protocol.equals("jar")){
                        JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
                        if(jarConnection != null) {
                            JarFile jarFile = jarConnection.getJarFile();
                            if(jarFile!=null){
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static void addClass(Set<Class<?>> classes, String packageName, String packagePath) {
    }
}
