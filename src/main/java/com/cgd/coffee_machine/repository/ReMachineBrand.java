package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.MachineBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReMachineBrand extends JpaRepository<MachineBrand, Long>, JpaSpecificationExecutor<MachineBrand> {

}