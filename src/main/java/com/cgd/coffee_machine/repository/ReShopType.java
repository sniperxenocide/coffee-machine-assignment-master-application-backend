package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.ShopType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReShopType extends JpaRepository<ShopType, Long>, JpaSpecificationExecutor<ShopType> {

}