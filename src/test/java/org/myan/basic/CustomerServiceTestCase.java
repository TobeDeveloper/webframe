package org.myan.basic;

import org.junit.Before;
import org.junit.Test;
import org.myan.web.util.DBUtil;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by myan on 2017/8/8.
 * Intellij IDEA
 */
public class CustomerServiceTestCase {

    private CustomerService service;

    @Before
    public void setUp() throws Exception {
        service = new CustomerService();
        //init database, load test sql file
        DBUtil.executeSqlFile("test_sql.sql");
    }

    @Test
    public void testGetCustomerList() {
        assertEquals(2, service.getCustomers().size());
    }

    @Test
    public void testGetCustomer() {
        assertNotNull(service.getCustomer(1));
    }

    @Test
    public void testCreateCustomer() {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("name", "myan");
        fieldMap.put("contact", "hannah");
        fieldMap.put("telephone", "13323008822");
        assertTrue(service.createCustomer(fieldMap));
    }

    @Test
    public void testUpdateCustomer() {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("name", "michael");
        assertTrue(service.updateCustomer(1, fieldMap));
    }

    @Test
    public void testDeleteCustomer() {
        assertTrue(service.deleteCustomer(1));
    }
}
