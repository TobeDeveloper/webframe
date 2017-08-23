package org.myan.web.mvc;

import org.myan.web.ConfigHelper;
import org.myan.web.annotation.HttpMethod;
import org.myan.web.beans.RequestHandler;
import org.myan.web.context.AbstractContext;
import org.myan.web.context.BeanContext;
import org.myan.web.context.WebApplicationContext;
import org.myan.web.mvc.file.FileUploader;
import org.myan.web.util.ClassUtil;
import org.myan.web.util.JsonUtil;
import org.myan.web.util.RefelectionUtil;

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
        //init FileUploader
        FileUploader.init(servletContext);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestHandler handler = getRequestHandler(req);
        if (handler != null && !handler.equals(WebApplicationContext.NOT_FOUND_HANDLER)) {
            Class<?> controllerClass = handler.getControllerClass();
            Object controller = BeanContext.getBean(controllerClass);

            Param param;
            if (FileUploader.isMultipart(req))
                param = FileUploader.createParam(req);
            else
                param = RequestParamCreator.createParam(req);
            Method actionMethod = handler.getRequestMethod();
            Object result = RefelectionUtil.invokeMethod(controller, actionMethod, param);

            processResult(req, resp, result);
        } else {
            //maybe we can't find certain mapped path
            if(handler.equals(WebApplicationContext.NOT_FOUND_HANDLER))
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private RequestHandler getRequestHandler(HttpServletRequest req) {
        String requestMethod = req.getMethod().toUpperCase();
        String requestPath = req.getPathInfo();
        RequestHandler handler = null;
        for (HttpMethod method : HttpMethod.values()) {
            if (method.name().equals(requestMethod)) {
                //we always got one method from client request
                handler = WebApplicationContext.getHandler(new HttpMethod[]{method}, requestPath);
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
                AbstractContext.class,
                BeanContext.class,
                WebApplicationContext.class,
        };

        for (Class<?> clazz : classList) {
            ClassUtil.loadClass(clazz.getName(), false);
        }
    }
}
