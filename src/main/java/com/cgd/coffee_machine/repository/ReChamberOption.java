package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.ChamberOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReChamberOption extends JpaRepository<ChamberOption, Long>, JpaSpecificationExecutor<ChamberOption> {

}