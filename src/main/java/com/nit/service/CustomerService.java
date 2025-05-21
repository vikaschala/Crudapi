package com.nit.service;

import java.util.List;

import com.nit.entity.Customer;

public interface CustomerService {

    List<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    Customer saveCustomer(Customer customer);

    void deleteCustomer(Long id);

	void updateCustomer(Long id, Customer updatedCustomer);
}
