package com.mahesh.controllers;

import com.mahesh.dto.CustomerDTO;
import com.mahesh.exception.NotFoundException;
import com.mahesh.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class CustomerController {

    private final CustomerService customerService;

    public static final String CUSTOMER_PATH = "/api/v1/customer";

    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listCustomer(){
        return customerService.getAllCustomer();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID Id){
        log.debug("Getting Customer details having Id: " + Id + " - By Controller");
        return customerService.getCustomerById(Id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity handlePost(@RequestBody CustomerDTO customer){
        CustomerDTO savedCustomer = customerService.saveCustomer(customer);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerDetails(@PathVariable("customerId") UUID Id, @RequestBody CustomerDTO customer){

        log.info("Updating Customer Details having customer Id: " + Id);

        if (customerService.updateCustomer(Id, customer).isEmpty()){
            throw new NotFoundException();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(customerService.getCustomerById(Id));
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID id){
        log.info("Customer deleted having customer Id: " + id);

        if (!customerService.deleteById(id)){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomerById(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO customer){

        customerService.patchCustomer(id, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
