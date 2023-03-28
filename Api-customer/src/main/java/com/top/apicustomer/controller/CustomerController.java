package com.top.apicustomer.controller;

import com.top.apicustomer.entity.Customer;
import com.top.apicustomer.event.CustomerEventProducer;
import com.top.apicustomer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    public CustomerService customerService;

    @Autowired
    public CustomerEventProducer customerEventProducer;

    @GetMapping("/{doctype}/{docnum}")
    public ResponseEntity<Optional<Customer>> getCustomer (@PathVariable String doctype, @PathVariable String docnum){
        ResponseEntity response = null;

        if(doctype != null && docnum != null){
            response = new ResponseEntity(customerService.findByDocument(doctype, docnum), HttpStatus.OK);
        } else response = new ResponseEntity("Missing parameters", HttpStatus.FORBIDDEN);

        return response;
    }

    @PostMapping("/save")
    public ResponseEntity<Customer> saveCustomer (@RequestBody Customer customer){
        ResponseEntity response = null;

        if(customer != null){
            customerEventProducer.publishCustomerEvent(new CustomerEventProducer.Data(customer.getDocType(), customer.getDocNum()));
            response = new ResponseEntity(customerService.saveCustomer(customer), HttpStatus.OK);
        } else response = new ResponseEntity("Customer is null", HttpStatus.FORBIDDEN);

        return response;
    }


    @PutMapping("/update")
    public ResponseEntity<Customer> updateCustomer (@RequestBody Customer customer){
        ResponseEntity response = null;

        if(customer != null){
            if(customer.getDocType() != null || customer.getDocNum() != null){
                response = new ResponseEntity("These data cannot be modified", HttpStatus.FORBIDDEN);
            } else response = new ResponseEntity(customerService.modifyCustomer(customer), HttpStatus.OK);
        } else response = new ResponseEntity("Customer is null", HttpStatus.FORBIDDEN);

        return response;
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCustomer (@PathVariable Long id){
        ResponseEntity response = null;

        if(id != null){
            response = new ResponseEntity(customerService.deleteCustomer(id), HttpStatus.OK);
        } else response = new ResponseEntity("Customer id is null", HttpStatus.FORBIDDEN);

        return response;
    }

}
