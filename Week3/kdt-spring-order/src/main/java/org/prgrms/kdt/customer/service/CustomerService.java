package org.prgrms.kdt.customer.service;

import org.prgrms.kdt.customer.Customer;

import java.util.List;

public interface CustomerService {
    void createCustomers(List<Customer> customers);
    List<Customer> getAllCustomers();
}
