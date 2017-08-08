package org.myan.basic;

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
    private static final String DRIVER;
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties conf = PropertyLoader.load("config.properties");
        DRIVER  = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USER = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.error("Can not load jdbc driver.",e);
        }
    }
    //TODO
    public List<Customer> getCustomers() {
        return null;
    }

    public Customer getCustomer(long id) {
        return null;
    }

    public boolean createCustomer(Map<String, Object> fieldMap) {
        return false;
    }

    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return false;
    }

    public boolean deleteCustomer(long id) {
        return false;
    }
}
