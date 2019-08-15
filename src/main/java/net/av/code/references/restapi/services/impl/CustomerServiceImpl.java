package net.av.code.references.restapi.services.impl;

import net.av.code.references.restapi.dtos.CustomerDTO;
import net.av.code.references.restapi.services.api.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.av.code.references.restapi.aop.annotations.Auditable;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);


    
    @Auditable
    public CustomerDTO getCustomer(Long id){
        return new CustomerDTO(id,"name","lastname","name.lastname@gmail.com");
    }
    
    @Auditable()
    public CustomerDTO createCustomer(CustomerDTO customer) {

        String traceInfo;

        LOG.info("Create Customer"+customer.toString());
        
        //search something --> trace info that you want to audit
        //getDataService().anotherInfo --> audit

        return customer;
    }

}