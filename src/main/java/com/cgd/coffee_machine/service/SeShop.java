package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.CgdCoffeeMachineModule;
import com.cgd.coffee_machine.model.*;
import com.cgd.coffee_machine.repository.ReContractHistory;
import com.cgd.coffee_machine.repository.ReOracleDistributor;
import com.cgd.coffee_machine.repository.ReResponsibleOfficer;
import com.cgd.coffee_machine.repository.ReShop;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
public class SeShop {
    private final ReShop repository;
    private final ReResponsibleOfficer reResponsibleOfficer;
    private final SeCommon seCommon;
    private final ReOracleDistributor reOracleDistributor;
    private final ReContractHistory reContractHistory;

    private final int pageSize = 10;
    private final Logger logger = CgdCoffeeMachineModule.LOGGER;

    public SeShop(ReShop repository, ReResponsibleOfficer reResponsibleOfficer, SeCommon seCommon, ReOracleDistributor reOracleDistributor, ReContractHistory reContractHistory) {
        this.repository = repository;
        this.reResponsibleOfficer = reResponsibleOfficer;
        this.seCommon = seCommon;
        this.reOracleDistributor = reOracleDistributor;
        this.reContractHistory = reContractHistory;
    }

    public ArrayList<ResponsibleOfficer> getAllMso(){
        return (ArrayList<ResponsibleOfficer>) reResponsibleOfficer.findAll();
    }

    public ArrayList<Shop> getAll(){
        return (ArrayList<Shop>) repository.findAll();
    }

    public long getShopCount(){return repository.count();}

    public int getLastPageNumber(){
        try {
            int cnt = (int)repository.count();
            return cnt%pageSize==0?cnt/pageSize:cnt/pageSize+1;
        }catch (Exception ignored){return 1;}
    }

    public Page<Shop> getAllPageable(Integer page){
        //System.out.println(repository.getTypeWiseShopSummary());
        int pageIndex = (page == null || page <= 1) ? 0 : Math.min(page,getLastPageNumber())-1 ;
        Pageable pageable = PageRequest.of( pageIndex , pageSize,
                Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(pageable);
    }

    public Shop getOne(Long id){
        if(id == null) return new Shop();
        return repository.findById(id).orElse(null);
    }

    public ArrayList<ContractHistory> getContractHistoryOfShop(Long shopId){
        return (ArrayList<ContractHistory>) reContractHistory.getAllByShopId(shopId,Sort.by(Sort.Direction.DESC,"id"));
    }

    public Shop getOneByShopCode(String shopCode){
        return repository.findByShopCode(shopCode).orElse(null);
    }

    public void saveShop(Shop shop, HttpServletRequest request) throws Exception{
        try {
            logger.info("Before Shop Create: "+shop);
            User user = seCommon.getUser(request);
            if(user == null) throw new Exception("Unauthorized Request");
            shop.setCreatedBy(user);
            repository.save(shop);
            logger.info("Shop Created: "+shop);
            insertDistributorInDb(shop);
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            throw e;
        }
    }

    public void updateShop(Shop shop) throws Exception{
        try {
            logger.info("Before Shop Update: "+shop);
            Shop prvShop = repository.findById(shop.getId()).orElse(null);
            if(prvShop == null) throw  new Exception("No Shop Profile found");
            shop.setCreatedBy(prvShop.getCreatedBy());
            shop.setCreationTime(prvShop.getCreationTime());
            repository.save(shop);
            logger.info("Shop Updated: "+shop);
            insertDistributorInDb(shop);
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            throw e;
        }
    }

    private void insertDistributorInDb(Shop shop){
        try {
            OracleDistributor distributor = reOracleDistributor.findByOracleCode(shop.getDistributorOracleCode()).orElse(null);
            if(distributor==null) distributor = new OracleDistributor();
            distributor.setOracleCode(shop.getDistributorOracleCode());
            distributor.setName(shop.getDistributorName());
            distributor.setDivision(shop.getDivision());
            distributor.setRegion(shop.getRegion());
            distributor.setTerritory(shop.getTerritory());
            if(distributor.getOracleCode().equalsIgnoreCase("null") || distributor.getOracleCode().trim().length()==0)
                throw new Exception("Invalid Distributor: "+distributor);
            reOracleDistributor.save(distributor);
            logger.info("New Distributor Added: "+distributor);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    public OracleDistributor getOracleDistributorByCode(String code) throws Exception{
        try {
            return reOracleDistributor.findByOracleCode(code).orElse(null);
        }catch (Exception e){
            logger.info(e.getMessage());
            throw e;
        }
    }
}
