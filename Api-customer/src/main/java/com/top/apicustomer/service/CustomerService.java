package com.top.apicustomer.service;

import com.top.apicustomer.entity.Customer;
import com.top.apicustomer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Optional<Customer> findByDocument(String doctype, String docnum) {
        return customerRepository.findByDocTypeAndDocNum(doctype, docnum);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public String deleteCustomer(Long id) {
        if(customerRepository.existsById(id)){
            customerRepository.deleteById(id);
            return "Customer deleted with id " + id;
        } else return "Customer id doesn't exist";
    }

    public Customer modifyCustomer(Customer customer) {
        Optional<Customer> c = customerRepository.findById(customer.getId());

        if(customer.getName() != null){
            c.get().setName(customer.getName());
        }
        if(customer.getSurname() != null){
            c.get().setSurname(customer.getSurname());
        }
        if(customer.getGender() != null){
            c.get().setGender(customer.getGender());
        }
        if(customer.getBirthDate() != null){
            c.get().setBirthDate(customer.getBirthDate());
        }

        return customerRepository.save(c.get());
    }

}
