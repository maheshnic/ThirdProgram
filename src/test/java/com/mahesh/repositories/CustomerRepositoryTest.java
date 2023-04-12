package com.mahesh.repositories;

import com.mahesh.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer(){
        Customer customer = customerRepository.save(Customer.builder()
                .customerName("Jay Prakash").build());

        assertThat(customer.getId()).isNotNull();
    }
}