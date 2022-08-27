package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.model.OracleDistributor;
import com.cgd.coffee_machine.model.Shop;
import com.cgd.coffee_machine.report.LocationWiseShopSummary;
import com.cgd.coffee_machine.report.MachineSummary;
import com.cgd.coffee_machine.report.TypeWiseShopSummary;
import com.cgd.coffee_machine.repository.ReMachine;
import com.cgd.coffee_machine.repository.ReOracleDistributor;
import com.cgd.coffee_machine.repository.ReShop;
import com.cgd.coffee_machine.request_response.MarketHierarchy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;

@Service
public class SeReport {
    private final ReMachine reMachine;
    private final ReShop reShop;
    private final ReOracleDistributor reOracleDistributor;

    public SeReport(ReMachine reMachine, ReShop reShop, ReOracleDistributor reOracleDistributor) {
        this.reMachine = reMachine;
        this.reShop = reShop;
        this.reOracleDistributor = reOracleDistributor;
    }

    public ArrayList<MachineSummary> getMachineSummary(){
        ArrayList<MachineSummary> list = (ArrayList<MachineSummary>) reMachine.findBrandCountryWiseMachineSummary();
        MachineSummary total = new MachineSummary("TOTAL","","",(long)0,(long)0,(long)0);
        for (MachineSummary ms:list) {
            total.setTotal(total.getTotal()+ms.getTotal());
            total.setTotalAssigned(total.getTotalAssigned()+ms.getTotalAssigned());
            total.setTotalUnassigned(total.getTotalUnassigned()+ms.getTotalUnassigned());
        }
        list.add(total);
        return  list;
    }

    public ArrayList<LocationWiseShopSummary> getTerritoryWiseShopSummary(){
        return  (ArrayList<LocationWiseShopSummary>) reShop.getTerritoryWiseShopSummary();
    }

    public ArrayList<LocationWiseShopSummary> getRegionWiseShopSummary(){
        return  (ArrayList<LocationWiseShopSummary>) reShop.getRegionWiseShopSummary();
    }

    public ArrayList<LocationWiseShopSummary> getDivisionWiseShopSummary(){
        ArrayList<LocationWiseShopSummary> list = (ArrayList<LocationWiseShopSummary>) reShop.getDivisionWiseShopSummary();
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
        return (ArrayList<Shop>) reShop.getAllShopByTerritory(ter);
    }

    public ArrayList<MarketHierarchy> getMarketHierarchyList(){
        return (ArrayList<MarketHierarchy>) reOracleDistributor.getAllMarket();
    }
}
