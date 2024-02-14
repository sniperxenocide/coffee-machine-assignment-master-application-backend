package com.cgd.coffee_machine.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
public class CnShop {

    @Autowired
    RestTemplate restTemplate;


    @GetMapping("/api/distributor")
    public ResponseEntity<Object> getDistributorByCode(@RequestParam String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("city","city123");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "http://10.10.1.108:8080/api/customer/detail?customerNumber="+code,
                HttpMethod.GET, entity, Object.class);
    }
}
