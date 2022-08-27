package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReContract extends JpaRepository<Contract, Long>, JpaSpecificationExecutor<Contract> {

}