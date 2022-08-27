package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.model.OriginCountry;
import com.cgd.coffee_machine.repository.ReOriginCountry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SeOriginCountry {
    private final ReOriginCountry repository;

    public SeOriginCountry(ReOriginCountry repository) {
        this.repository = repository;
    }

    public ArrayList<OriginCountry> getAll(){
        return (ArrayList<OriginCountry>) repository.findAll();
    }
}
