package org.myan.web.mvc;

import org.myan.web.annotation.HttpMethod;
import org.myan.web.beans.BeanContext;
import org.myan.web.beans.RequestHandler;
import org.myan.web.helper.ClassHelper;
import org.myan.web.helper.ConfigHelper;
import org.myan.web.helper.ControllerHelper;
import org.myan.web.util.ClassUtil;
import org.myan.web.util.CodecUtil;
import org.myan.web.util.CollectionUtil;
import org.myan.web.util.JsonUtil;
import org.myan.web.util.RefelectionUtil;
import org.myan.web.util.StreamUtil;
import org.myan.web.util.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
        RequestHandler handler = getRequestHandler(req);
        if (handler != null) {
            Class<?> controllerClass = handler.getControllerClass();
            Object controller = BeanContext.getBean(controllerClass);
            //we should set all the request params
            Map<String, Object> paramsMap = new HashMap<>();
            Enumeration<String> paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                paramsMap.put(paramName, req.getParameter(paramName));
            }

            //add url params
            String requestBody = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if (StringUtil.isNotEmpty(requestBody)) {
                String[] params = StringUtil.splitString(requestBody, "&");
                if (CollectionUtil.isNotEmpty(params)) {
                    for (String param : params) {
                        String[] kv = StringUtil.splitString(param, "=");
                        if (CollectionUtil.isNotEmpty(kv) && kv.length == 2) {
                            paramsMap.put(kv[0], kv[1]);
                        }
                    }
                }
            }

            Param param = new Param(paramsMap);
            Method actionMethod = handler.getRequestMethod();
            Object result = RefelectionUtil.invokeMethod(controller, actionMethod, param);

            processResult(req, resp, result);
        }
    }

    private RequestHandler getRequestHandler(HttpServletRequest req) {
        String requestMethod = req.getMethod().toUpperCase();
        String requestPath = req.getPathInfo();
        HttpMethod.values();
        RequestHandler handler = null;
        for (HttpMethod method : HttpMethod.values()) {
            if (method.name().equals(requestMethod)) {
                //we always got one method from client request
                handler = ControllerHelper.getHandler(new HttpMethod[]{method}, requestPath);
                break;
            }

        }
        return handler;
    }

    private void processResult(HttpServletRequest req, HttpServletResponse resp, Object result) throws IOException, ServletException {
        if (result instanceof View) {
            View view = (View) result;
            String path = view.getPath();
            if (path.startsWith("/")) {
                resp.sendRedirect(req.getContextPath() + path);
            } else {
                Map<String, Object> model = view.getDataModels();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    req.setAttribute(entry.getKey(), entry.getValue());
                }
                req.getRequestDispatcher(ConfigHelper.getJspPath() + path).forward(req, resp);
            }
        } else if (result instanceof DataResult) {
            DataResult dataResult = (DataResult) result;
            Object model = dataResult.getDataModel();
            if (model != null) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter writer = resp.getWriter();
                writer.write(JsonUtil.toJson(model));
                writer.flush();
                writer.close();
            }
        }
    }
}

class Initializer {

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
