package org.myan.basic.ctrl;

import org.myan.basic.CustomerService;

/**
 * Created by myan on 2017/8/9.
 * Intellij IDEA
 */
@Controller
public class CustomerController {
    @Inject
    private CustomerService service;

    @Action(method=HttpMethod.GET, url="/customer")
    public View index(){
        View view = new View("customer.jsp");
        view.addModel("customers", service.getCustomers());
        return view;
    }

    @Action(method=HttpMethod.DELETE, url="/customer/delete")
    public DataResult delete(long id){
        return new DataResult(service.deleteCustomer(id));
    }
}
