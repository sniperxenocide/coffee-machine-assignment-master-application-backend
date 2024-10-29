package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.ContractHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReContractHistory extends JpaRepository<ContractHistory, Long>, JpaSpecificationExecutor<ContractHistory> {
    List<ContractHistory> getAllByShopId(Long shopId, Sort sort);
    List<ContractHistory> getAllByMachineId(Long machineId, Sort sort);
}