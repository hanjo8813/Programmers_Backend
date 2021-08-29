package org.prgrms.kdt.customer;

import org.prgrms.kdt.customer.Repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(isolation= Isolation.DEFAULT )
    public void createCustomers(List<Customer> customers) {
        customers.forEach(customerRepository::insert);
    }
}
