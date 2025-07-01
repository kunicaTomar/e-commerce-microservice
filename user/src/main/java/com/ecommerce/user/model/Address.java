package com.ecommerce.user.model;


import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {
    @Id
    private Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    
    
}
