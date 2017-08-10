package org.myan.web.mvc;

import org.myan.web.annotation.HttpMethod;
import org.myan.web.beans.BeanContext;
import org.myan.web.beans.RequestHandler;
import org.myan.web.helper.ClassHelper;
import org.myan.web.helper.ConfigHelper;
import org.myan.web.helper.ControllerHelper;
import org.myan.web.util.ClassUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by myan on 2017/8/10.
 * Intellij IDEA
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        //basic beans load here.
        Initializer.init();
        //prepare some jsp & resource servlet.
        ServletContext servletContext = config.getServletContext();
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getJspPath() + "*");

        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getResourcePath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod = req.getMethod().toUpperCase();
        String requestPath = req.getPathInfo();
        HttpMethod.values();
        RequestHandler handler;
        for (HttpMethod method : HttpMethod.values()) {
            if (method.name().equals(requestMethod))
                handler = ControllerHelper.getHandler(new HttpMethod[]{method}, requestPath);
        }
    }
}

class Initializer{

    static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                ControllerHelper.class,
                BeanContext.class

        };
        for (Class<?> clazz : classList) {
            ClassUtil.loadClass(clazz.getName(), false);
        }
    }
}
