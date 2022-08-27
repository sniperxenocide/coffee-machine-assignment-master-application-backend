package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.model.InstallmentType;
import com.cgd.coffee_machine.repository.ReInstallmentType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SeInstallmentType {
    private final ReInstallmentType repository;


    public SeInstallmentType(ReInstallmentType repository) {
        this.repository = repository;
    }

    public ArrayList<InstallmentType> getAll(){
        return (ArrayList<InstallmentType>) repository.findAll();
    }
}
