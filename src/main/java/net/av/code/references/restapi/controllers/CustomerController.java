package net.av.code.references.restapi.controllers;

import net.av.code.references.restapi.dtos.CustomerDTO;
import net.av.code.references.restapi.services.api.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private static Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @RequestMapping(method=RequestMethod.GET, value="/customers/{id}")
    public ResponseEntity<CustomerDTO> getCustomerByID(@PathVariable("id") long id){
       return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @RequestMapping(method=RequestMethod.GET, value="/customer/{id}")
    @ResponseBody
    public CustomerDTO getCustomerFirstByID(@PathVariable("id") long id){
        return customerService.getCustomer(id);
     }

    @RequestMapping(method=RequestMethod.GET, value="/customers/export")
    public String getCustomerSecondByID(@RequestParam("file") String file){
        LOG.info("Request Param for Customer: "+file);
        return "Success";
     }

     @RequestMapping(method=RequestMethod.POST, value="/customer/add")
     @ResponseBody
     public CustomerDTO createCustomer(@RequestBody CustomerDTO customer){
         LOG.info("Customer: "+customer.toString());
         return customerService.createCustomer(customer);
     }

}