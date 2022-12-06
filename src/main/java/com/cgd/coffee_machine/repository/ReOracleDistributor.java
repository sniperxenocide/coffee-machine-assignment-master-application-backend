package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.OracleDistributor;
import com.cgd.coffee_machine.dto.MarketHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReOracleDistributor extends JpaRepository<OracleDistributor, Long>, JpaSpecificationExecutor<OracleDistributor> {

    @Query(value = "select distinct new com.cgd.coffee_machine.dto.MarketHierarchy(od.division,od.region,od.territory) " +
            "from OracleDistributor as od ")
    List<MarketHierarchy> getAllMarket();

    Optional<OracleDistributor> findByOracleCode(String oracleCode);
}