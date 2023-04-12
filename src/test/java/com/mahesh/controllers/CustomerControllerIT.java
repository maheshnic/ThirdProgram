package com.mahesh.controllers;

import com.mahesh.dto.CustomerDTO;
import com.mahesh.entities.Customer;
import com.mahesh.exception.NotFoundException;
import com.mahesh.mappers.CustomerMapper;
import com.mahesh.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void testListCustomers(){
        List<CustomerDTO> dtos = customerController.listCustomer();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList(){
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.listCustomer();

        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void testGetById(){
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());

        assertThat(customerDTO).isNotNull();
    }

    @Test
    void testCustomerIdNotFound(){
        assertThrows(NotFoundException.class, () -> {
           customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testHandlePost(){
        CustomerDTO customerDTO = CustomerDTO.builder().customerName("Mahesh Kumar").build();

        ResponseEntity responseEntity = customerController.handlePost(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateCustomerDetails(){
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);

        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "Mahesh Kumar";
        customerDTO.setCustomerName(customerName);

        ResponseEntity responseEntity = customerController.updateCustomerDetails(customer.getId(), customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);
    }

    @Test
    void testUpdateNotFound(){
        assertThrows(NotFoundException.class, () -> {
           customerController.updateCustomerDetails(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteCustomerById(){
        Customer customer = customerRepository.findAll().get(0);

        ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(202));

        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void testDeleteByIdNotFound(){
        assertThrows(NotFoundException.class, () -> {
           customerController.deleteCustomerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testPatchById(){
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);

        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "Patched Customer";
        customerDTO.setCustomerName(customerName);

        ResponseEntity responseEntity = customerController.patchCustomerById(customer.getId(), customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo(updatedCustomer.getCustomerName());
    }

}