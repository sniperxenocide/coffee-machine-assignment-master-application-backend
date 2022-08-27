package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.model.ShopGrade;
import com.cgd.coffee_machine.model.ShopType;
import com.cgd.coffee_machine.repository.ReShopType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SeShopType {
    private final ReShopType repository;

    public SeShopType(ReShopType repository) {
        this.repository = repository;
    }

    public ArrayList<ShopType> getAll(){
        return (ArrayList<ShopType>) repository.findAll();}
}
