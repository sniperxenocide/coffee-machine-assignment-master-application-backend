package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.CgdCoffeeMachineModule;
import com.cgd.coffee_machine.dto.ContractDeleteDto;
import com.cgd.coffee_machine.model.*;
import com.cgd.coffee_machine.repository.ReContract;
import com.cgd.coffee_machine.repository.ReContractHistory;
import com.cgd.coffee_machine.repository.ReMachine;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class SeContract {
    private final ReContract repository;
    private final SeCommon seCommon;
    private final SeShop seShop;
    private final SeMachine seMachine;
    private final ReMachine reMachine;
    private final SePaymentTerm sePaymentTerm;
    private final SeInstallmentType seInstallmentType;
    private final ReContractHistory reContractHistory;
    public final Logger logger = CgdCoffeeMachineModule.LOGGER;

    public SeContract(ReContract repository, SeCommon seCommon, SeShop seShop, SeMachine seMachine, ReMachine reMachine, SePaymentTerm sePaymentTerm, SeInstallmentType seInstallmentType, ReContractHistory reContractHistory) {
        this.repository = repository;
        this.seCommon = seCommon;
        this.seShop = seShop;
        this.seMachine = seMachine;
        this.reMachine = reMachine;
        this.sePaymentTerm = sePaymentTerm;
        this.seInstallmentType = seInstallmentType;
        this.reContractHistory = reContractHistory;
    }

    public Contract getOne(Long id){
        if(id == null) return new Contract();
        return repository.findById(id).orElse(null);
    }

    public Shop getShop(Long shopId) throws Exception{
        Shop shop = seShop.getOne(shopId);
        if(shop == null) throw new Exception("Shop not Found");
        return shop;
    }


    public ArrayList<Machine> getAvailableMachines(){
        return seMachine.getAvailableMachines(2);
    }


    public Machine getMachineWithNoContract(Long machineId) throws Exception{
        Machine machine = seMachine.getOne(machineId);
        if(machine==null) throw new Exception("Machine Not Found");
        if(machine.getContract()!=null) throw new Exception("This Coffee Machine is Already Assigned to another Shop");
        return machine;
    }

    public void saveContract(Contract contract, HttpServletRequest request,String machineNumber) throws Exception{
        logger.info("Before Contract Create: "+contract);
        User user = seCommon.getUser(request);
        if(user == null) throw new Exception("Unauthorized Request");
        contract.setCreatedBy(user);
        if(contract.getMachine() == null) throw new Exception("Must Select a Coffee Machine");
        if(machineNumber==null || machineNumber.trim().length()==0){
            if(contract.getMachine().getMachineNumber()==null || contract.getMachine().getMachineNumber().trim().length()==0){
                throw new Exception("Must Provide Machine Serial Number");
            }
            else machineNumber = contract.getMachine().getMachineNumber();
        }
        else {   //Checking Duplicate Machine Serial Number
            if(!Objects.equals(contract.getMachine().getMachineNumber(), machineNumber) &&
                    reMachine.getByMachineNumber(machineNumber).isPresent())
                throw new Exception("Machine Already Exist with this Machine Number "+machineNumber);
        }
        repository.save(contract);
        contract.getMachine().setMachineNumber(machineNumber);
        reMachine.save(contract.getMachine());
        logger.info("Contract Created: "+contract);
    }

    public void updateContract(Contract contract,HttpServletRequest request,String machineNumber,String changeReason) throws Exception{
        User user = seCommon.getUser(request);
        if(user == null) throw new Exception("Unauthorized Request");
        try {
            logger.info("Before Contract Update: {}", contract);
            Contract prvContract = repository.findById(contract.getId()).orElse(null);
            if(prvContract == null) throw  new Exception("No Contract found");
            contract.setCreatedBy(prvContract.getCreatedBy());
            contract.setCreationTime(prvContract.getCreationTime());
            System.out.println(contract);
            if(prvContract.getMachine().getId().longValue() != contract.getMachine().getId()){
                //Handle Machine Change and Save Change History
                if(machineNumber==null || machineNumber.trim().isEmpty()){
                    if(contract.getMachine().getMachineNumber()==null || contract.getMachine().getMachineNumber().trim().isEmpty()){
                        throw new Exception("Must Provide Machine Serial Number");
                    }
                    else machineNumber = contract.getMachine().getMachineNumber();
                }

                ContractDeleteDto deleteDto = new ContractDeleteDto();
                deleteDto.setDeleteReason(changeReason);
                ContractHistory contractHistory = new ContractHistory(prvContract,user,deleteDto);
                reContractHistory.save(contractHistory);
                logger.info("Contract History Saved.History ID:  "+contractHistory.getId());
            }
            repository.save(contract);
            if(machineNumber!=null && !machineNumber.trim().isEmpty()){
                contract.getMachine().setMachineNumber(machineNumber);
                reMachine.save(contract.getMachine());
            }
            logger.info("Contract Updated: {}", contract);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteContract(HttpServletRequest request, Contract contract, ContractDeleteDto deleteDto) throws Exception{
        User user = seCommon.getUser(request);
        if(user == null) throw new Exception("Unauthorized Request");
        try {
            ContractHistory contractHistory = new ContractHistory(contract,user,deleteDto);
            contractHistory = reContractHistory.save(contractHistory);
            logger.info("Contract History Saved.History ID:  "+contractHistory.getId());
            repository.delete(contract);
            logger.info("Contract Deleted."+contractHistory);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<PaymentTerm> getAllPaymentTerm(){
           return sePaymentTerm.getAll();
    }

    public ArrayList<InstallmentType> getAllInstallmentType(){
        return seInstallmentType.getAll();
    }


}
