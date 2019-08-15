package net.av.code.references.restapi.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CustomerDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private long code=0;
    private String fisrtname="";
    private String lastname="";
    private String mail="";

    /*
    public CustomerDTO(long code, String firstname, String lastname, String mail){
        this.code=code; this.fisrtname=firstname; this.lastname=lastname; this.mail=mail;
    }
     */

}