package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.ShopGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReShopGrade extends JpaRepository<ShopGrade, Long>, JpaSpecificationExecutor<ShopGrade> {

}