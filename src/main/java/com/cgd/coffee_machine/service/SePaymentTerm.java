package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.model.PaymentTerm;
import com.cgd.coffee_machine.repository.RePaymentTerm;
import com.cgd.coffee_machine.request_response.Response;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SePaymentTerm {
    private final RePaymentTerm repository;

    public SePaymentTerm(RePaymentTerm repository) {
        this.repository = repository;
    }

    public ArrayList<PaymentTerm> getAll(){
        return (ArrayList<PaymentTerm>) repository.findAll();
    }

    public Response getOneRest(Long id){
        PaymentTerm paymentTerm = repository.findById(id).orElse(null);
        if(paymentTerm==null) return new Response(false,"Not Found");
        return new Response(true,"Success",paymentTerm);
    }

    public Response getAllRest(){
        return new Response(true,"Success",repository.findAll());
    }
}
