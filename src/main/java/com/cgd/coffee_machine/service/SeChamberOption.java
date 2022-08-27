package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.model.ChamberOption;
import com.cgd.coffee_machine.repository.ReChamberOption;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SeChamberOption {
    private final ReChamberOption repository;


    public SeChamberOption(ReChamberOption repository) {
        this.repository = repository;
    }

    public ArrayList<ChamberOption> getAll(){
        return (ArrayList<ChamberOption>) repository.findAll();
    }

    public ChamberOption getOne(Long id){
        return repository.findById(id).orElse(null);
    }
}
