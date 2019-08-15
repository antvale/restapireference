package net.av.code.references.restapi.services.api;

import net.av.code.references.restapi.dtos.CustomerDTO;

public interface CustomerService {

    public CustomerDTO getCustomer(Long id);

    public CustomerDTO createCustomer(CustomerDTO customer);

}