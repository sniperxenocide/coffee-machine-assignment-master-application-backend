package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.model.MachineBrand;
import com.cgd.coffee_machine.repository.ReMachineBrand;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SeMachineBrand {
    private final ReMachineBrand repository;


    public SeMachineBrand(ReMachineBrand repository) {
        this.repository = repository;
    }

    public ArrayList<MachineBrand> getAll(){
        return (ArrayList<MachineBrand>) repository.findAll();
    }
}
