package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.model.Shop;
import com.cgd.coffee_machine.report.LocationWiseShopSummary;
import com.cgd.coffee_machine.report.MachineSummary;
import com.cgd.coffee_machine.report.TypeWiseShopSummary;
import com.cgd.coffee_machine.repository.ReMachine;
import com.cgd.coffee_machine.repository.ReOracleDistributor;
import com.cgd.coffee_machine.repository.ReShop;
import com.cgd.coffee_machine.dto.MarketHierarchy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service @Slf4j
public class SeReport {
    private final ReMachine reMachine;
    private final ReShop reShop;
    private final ReOracleDistributor reOracleDistributor;

    public SeReport(ReMachine reMachine, ReShop reShop, ReOracleDistributor reOracleDistributor) {
        this.reMachine = reMachine;
        this.reShop = reShop;
        this.reOracleDistributor = reOracleDistributor;
    }

    public ArrayList<MachineSummary> getMachineSummary(String startDate,String endDate){
        LocalDateTime start = LocalDateTime.parse(startDate+"T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T00:00:00").plusDays(1);
        return  (ArrayList<MachineSummary>) reMachine.findBrandCountryWiseMachineSummary(start,end);
    }

    public ArrayList<LocationWiseShopSummary> getTerritoryWiseShopSummary(String startDate,String endDate){
        LocalDateTime start = LocalDateTime.parse(startDate+"T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T00:00:00").plusDays(1);
        return  (ArrayList<LocationWiseShopSummary>) reShop.getTerritoryWiseShopSummary(start,end);
    }


    public ArrayList<TypeWiseShopSummary> getTypeWiseShopSummary(){
        return (ArrayList<TypeWiseShopSummary>) reShop.getTypeWiseShopSummary();
    }

    public ArrayList<Shop> getShopDetailList(String division,String region,String territory){
        log.info("Division {},Region {},Territory {}", division, region, territory);
        if (division.equals("-")) division = "%";
        if (region.equals("-") || region.isEmpty()) region = "%";
        if (territory.equals("-") || territory.isEmpty()) territory = "%";
        return (ArrayList<Shop>) reShop.getAllShopByTerritory(division,region,territory);
    }

    public ArrayList<MarketHierarchy> getMarketHierarchyList(){
        return (ArrayList<MarketHierarchy>) reOracleDistributor.getAllMarket();
    }
}
