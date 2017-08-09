package org.myan.basic;

import org.myan.util.DBUtil;
import org.myan.util.PropertyLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by myan on 2017/8/8.
 * Intellij IDEA
 */
public class CustomerService {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    public List<Customer> getCustomers() {
        String sql = "SELECT * FROM customer";
        return DBUtil.queryEntityList(Customer.class, sql);
    }

    public Customer getCustomer(long id) {
        String sql = "SELECT * FROM customer WHERE id=?";
        return DBUtil.queryEntityList(Customer.class, sql, id).get(0);
    }

    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DBUtil.insert(Customer.class, fieldMap);
    }

    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DBUtil.update(Customer.class, id, fieldMap);
    }

    public boolean deleteCustomer(long id) {
        return DBUtil.delete(Customer.class, id);
    }
}
