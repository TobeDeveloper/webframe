package org.myan.basic.ctrl;

import org.myan.basic.Customer;
import org.myan.basic.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by myan on 2017/8/9.
 * Intellij IDEA
 */
@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {
    private CustomerService service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = new CustomerService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Customer> customers = service.getCustomers();
        req.setAttribute("customers", customers);
        req.getRequestDispatcher("WEB-INF/jsp/customer.jsp").forward(req, resp);
    }
}
