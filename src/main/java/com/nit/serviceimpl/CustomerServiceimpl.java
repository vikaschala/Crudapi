package com.nit.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nit.entity.Customer;
import com.nit.repository.CustomerRepository;
import com.nit.service.CustomerService;

@Service
@Transactional
public class CustomerServiceimpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        if (customer.getEmail() != null && !customer.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        return customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(Long id, Customer updatedCustomer) {
        Customer existing = getCustomerById(id);
        if (existing != null) {
            existing.setName(updatedCustomer.getName());
            existing.setEmail(updatedCustomer.getEmail());
            existing.setPhone(updatedCustomer.getPhone());
            customerRepository.save(existing);
        } else {
            throw new RuntimeException("Customer not found with ID: " + id);
        }
    }

    @Override
    public void deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        } else {
            throw new RuntimeException("Customer not found with ID: " + id);
        }
    }
}
