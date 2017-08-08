package org.myan.basic;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by myan on 2017/8/8.
 * Intellij IDEA
 */
public class CustomerServiceTestCase {

    private CustomerService service;

    @Before
    public void setUp() {
        service = new CustomerService();
        //init database
    }

    @Test
    public void testGetCustomerList(){
        assertEquals(2, service.getCustomers().size());
    }

    @Test
    public void testGetCustomer(){
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
    public void testDeleteCustomer(){
        assertTrue(service.deleteCustomer(1));
    }
}
