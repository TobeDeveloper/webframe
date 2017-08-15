package org.myan.basic.ctrl;

import org.myan.basic.CustomerService;
import org.myan.web.annotation.Action;
import org.myan.web.annotation.Aspect;
import org.myan.web.annotation.Controller;
import org.myan.web.annotation.HttpMethod;
import org.myan.web.annotation.Inject;
import org.myan.web.mvc.DataResult;
import org.myan.web.mvc.Param;
import org.myan.web.mvc.View;

/**
 * Created by myan on 2017/8/9.
 * Intellij IDEA
 */
@Controller
public class CustomerController {
    @Inject
    private CustomerService service;

    @Action(method = HttpMethod.GET, path = "/customer")
    public View index() {
        View view = new View("customer.jsp");
        view.addModel("customers", service.getCustomers());
        return view;
    }

    @Action(method = HttpMethod.DELETE, path = "/customer/delete")
    public DataResult delete(Param p) {
        return new DataResult(service.deleteCustomer(p.getLong("id")));
    }
}
