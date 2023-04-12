package com.mahesh.service;

import com.mahesh.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDTO> getAllCustomer();

    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO saveCustomer(CustomerDTO customer);

    Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customer);

    Boolean deleteById(UUID id);

    Optional<CustomerDTO> patchCustomer(UUID id, CustomerDTO customer);
}
