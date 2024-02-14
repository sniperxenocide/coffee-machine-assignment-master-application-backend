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
        LocalDateTime end = LocalDateTime.parse(endDate+"T00:00:00").plus(1, ChronoUnit.DAYS);
        ArrayList<MachineSummary> list = (ArrayList<MachineSummary>) reMachine.findBrandCountryWiseMachineSummary(start,end);
        MachineSummary total = new MachineSummary("TOTAL","","",(long)0,(long)0,(long)0);
        for (MachineSummary ms:list) {
            total.setTotal(total.getTotal()+ms.getTotal());
            total.setTotalAssigned(total.getTotalAssigned()+ms.getTotalAssigned());
            total.setTotalUnassigned(total.getTotalUnassigned()+ms.getTotalUnassigned());
        }
        list.add(total);
        return  list;
    }

    public ArrayList<LocationWiseShopSummary> getTerritoryWiseShopSummary(String startDate,String endDate){
        LocalDateTime start = LocalDateTime.parse(startDate+"T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate+"T00:00:00").plus(1, ChronoUnit.DAYS);
        return  (ArrayList<LocationWiseShopSummary>) reShop.getTerritoryWiseShopSummary(start,end);
    }

    public ArrayList<LocationWiseShopSummary> getRegionWiseShopSummary(String startDate,String endDate){
        LocalDateTime start = LocalDateTime.parse(startDate+"T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate+"T00:00:00").plus(1, ChronoUnit.DAYS);
        return  (ArrayList<LocationWiseShopSummary>) reShop.getRegionWiseShopSummary(start,end);
    }

    public ArrayList<LocationWiseShopSummary> getDivisionWiseShopSummary(String startDate,String endDate){
        LocalDateTime start = LocalDateTime.parse(startDate+"T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate+"T00:00:00").plus(1, ChronoUnit.DAYS);
        ArrayList<LocationWiseShopSummary> list =
                (ArrayList<LocationWiseShopSummary>) reShop.getDivisionWiseShopSummary(start,end);
        LocationWiseShopSummary total = new LocationWiseShopSummary("TOTAL","","",(long)0,(long)0,(long)0);
        for(LocationWiseShopSummary ls:list){
            total.setTotalShop(total.getTotalShop()+ ls.getTotalShop());
            total.setTotalMachine(total.getTotalMachine()+ ls.getTotalMachine());
            total.setTotalUnassignedShop(total.getTotalUnassignedShop()+ls.getTotalUnassignedShop());
        }
        list.add(total);
        return  list;
    }

    public ArrayList<TypeWiseShopSummary> getTypeWiseShopSummary(){
        ArrayList<TypeWiseShopSummary> list = (ArrayList<TypeWiseShopSummary>) reShop.getTypeWiseShopSummary();
        TypeWiseShopSummary total = new TypeWiseShopSummary("TOTAL",(long)0,(long)0,(long)0);
        for (TypeWiseShopSummary ts:list) {
            total.setTotalShop(total.getTotalShop()+ts.getTotalShop());
            total.setTotalMachine(total.getTotalMachine()+ts.getTotalMachine());
            total.setTotalUnassignedShop(total.getTotalUnassignedShop()+ts.getTotalUnassignedShop());
        }
        list.add(total);
        return  list;
    }

    public ArrayList<Shop> getShopDetailList(String territory){
        String ter = territory;
        if (territory==null || territory.length()==0) return new ArrayList<>();
        if(territory.equals("-")) ter="%";
        log.info("Territory {}",ter);
        return (ArrayList<Shop>) reShop.getAllShopByTerritory(ter);
    }

    public ArrayList<MarketHierarchy> getMarketHierarchyList(){
        return (ArrayList<MarketHierarchy>) reOracleDistributor.getAllMarket();
    }
}
