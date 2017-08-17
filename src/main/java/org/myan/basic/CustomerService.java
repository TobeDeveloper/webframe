package org.myan.basic;

import org.myan.web.annotation.Service;
import org.myan.web.annotation.Transactional;
import org.myan.web.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by myan on 2017/8/8.
 * Intellij IDEA
 */
@Service
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

    @Transactional
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DBUtil.insert(Customer.class, fieldMap);
    }

    @Transactional
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DBUtil.update(Customer.class, id, fieldMap);
    }

    @Transactional
    public boolean deleteCustomer(long id) {
        return DBUtil.delete(Customer.class, id);
    }
}
