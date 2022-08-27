package com.cgd.coffee_machine.controller.rest;

import com.cgd.coffee_machine.service.SePaymentTerm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CnPaymentTerm {
    private final SePaymentTerm service;

    public CnPaymentTerm(SePaymentTerm service) {
        this.service = service;
    }

    @GetMapping("/api/v1/payment_term")
    public ResponseEntity<Object> getPaymentTerm(@RequestParam Long id){
        return new ResponseEntity<>(service.getOneRest(id), HttpStatus.OK);
    }

    @GetMapping("/api/v1/payment_term/all")
    public ResponseEntity<Object> getPaymentTermAll(){
        return new ResponseEntity<>(service.getAllRest(), HttpStatus.OK);
    }
}
