package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.InstallmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReInstallmentType extends JpaRepository<InstallmentType, Long>, JpaSpecificationExecutor<InstallmentType> {

}