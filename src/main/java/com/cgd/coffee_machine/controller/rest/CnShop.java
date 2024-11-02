package com.cgd.coffee_machine.controller.rest;

import com.cgd.coffee_machine.dto.Response;
import com.cgd.coffee_machine.model.OracleDistributor;
import com.cgd.coffee_machine.service.SeShop;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
@RestController
public class CnShop {

    @Autowired
    RestTemplate restTemplate;

    private final SeShop shopService;

    @Value("${oracle-service-url}")
    private String oracleServiceUrl;


    @GetMapping("/api/distributor")
    public ResponseEntity<Object> getDistributorByCode(@RequestParam String code) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("city","city123");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = oracleServiceUrl+"/api/customer/detail?customerNumber="+code;

        OracleDistributor distributor = shopService.getOracleDistributorByCode(code);

        return distributor!=null?
                ResponseEntity.ok(new Response(true,"Success",distributor)):
                restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
    }
}
