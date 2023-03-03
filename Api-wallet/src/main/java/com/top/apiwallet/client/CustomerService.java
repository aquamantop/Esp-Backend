package com.top.apiwallet.client;

import com.top.apiwallet.config.LoadBalancerConfiguration;
import com.top.apiwallet.model.CustomerDTO;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

@FeignClient(name = "api-customer")
@LoadBalancerClient(value = "api-customer", configuration = LoadBalancerConfiguration.class)
public interface CustomerService {

    @GetMapping("/customer/{doctype}/{docnum}")
    Optional<CustomerDTO> getCustomer (@PathVariable String doctype, @PathVariable String docnum);

}
