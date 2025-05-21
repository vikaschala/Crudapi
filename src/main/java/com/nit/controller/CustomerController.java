package com.nit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.nit.entity.Customer;
import com.nit.service.CustomerService;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

  
    @GetMapping
    public String getAllCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers";  
    }


    @GetMapping("/show-customer")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "add-customer";  
    }


    @PostMapping("/add")
    public String createCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.saveCustomer(customer);
        return "redirect:/customers";
    }


    @GetMapping("/edit/{id}")
    public String showEditCustomerForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            model.addAttribute("customer", customer);
            return "edit-customer";  
        } else {
            return "redirect:/customers";
        }
    }


    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute("customer") Customer updatedCustomer) {
        customerService.updateCustomer(id, updatedCustomer); 
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
