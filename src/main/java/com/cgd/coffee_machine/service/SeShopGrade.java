package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.model.OriginCountry;
import com.cgd.coffee_machine.model.ShopGrade;
import com.cgd.coffee_machine.repository.ReShopGrade;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SeShopGrade {
    private final ReShopGrade repository;

    public SeShopGrade(ReShopGrade repository) {
        this.repository = repository;
    }

    public ArrayList<ShopGrade> getAll(){
        return (ArrayList<ShopGrade>) repository.findAll();}
}
