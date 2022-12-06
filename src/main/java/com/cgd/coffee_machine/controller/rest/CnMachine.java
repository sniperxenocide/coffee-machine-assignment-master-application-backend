package com.cgd.coffee_machine.controller.rest;


import com.cgd.coffee_machine.service.SeMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;

@RestController
public class CnMachine {

    private final SeMachine service;

    @Autowired
    RestTemplate restTemplate;

    public CnMachine(SeMachine service) {
        this.service = service;
    }

    @GetMapping("/api/supplier")
    public ResponseEntity<Object> getSupplierByCode(@RequestParam Long code){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("city","city123");
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        return restTemplate.exchange(
                "http://10.10.1.108:8080/api/supplier?code="+code,
                HttpMethod.GET, entity, Object.class);
    }

    @GetMapping("/api/machine/update/machine_number")
    public ResponseEntity<Object> updateMachineNumber(@RequestParam Long id,@RequestParam String machineNumber){
        return new ResponseEntity<>(service.updateMachineNumber(id,machineNumber),HttpStatus.OK);
    }


}
