package com.mahesh.mappers;

import com.mahesh.dto.CustomerDTO;
import com.mahesh.entities.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);
}
