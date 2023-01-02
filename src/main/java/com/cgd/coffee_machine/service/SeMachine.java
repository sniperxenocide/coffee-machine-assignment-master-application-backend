package com.cgd.coffee_machine.service;

import com.cgd.coffee_machine.CgdCoffeeMachineModule;
import com.cgd.coffee_machine.model.Machine;
import com.cgd.coffee_machine.model.User;
import com.cgd.coffee_machine.repository.ReMachine;
import com.cgd.coffee_machine.dto.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class SeMachine {
    private final ReMachine repository;
    private final SeCommon seCommon;
    private final int pageSize = 10;
    private final Logger logger = CgdCoffeeMachineModule.LOGGER;

    public SeMachine(ReMachine repository, SeCommon seCommon) {
        this.repository = repository;
        this.seCommon = seCommon;
    }

    public ArrayList<Machine> getAll(){
        return (ArrayList<Machine>) repository.findAll();
    }

    public long getMachineCount(){return repository.count();}

    public int getLastPageNumber(){
        try {
            int cnt = (int)repository.count();
            return cnt%pageSize==0?cnt/pageSize:cnt/pageSize+1;
        }catch (Exception ignored){return 1;}
    }


    public ArrayList<Machine> getAvailableMachines(int count){
        return (ArrayList<Machine>) repository.getMachineForEachBrandWithNoContract(count);
    }

    public Page<Machine> getAllPageable(Integer page){
        int pageIndex = (page == null || page <= 1) ? 0 : Math.min(page,getLastPageNumber())-1 ;
        Pageable pageable = PageRequest.of( pageIndex , pageSize,
                Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(pageable);
    }

    public Page<Machine> getMachines(String id,String machineNumber,Integer page){
        int pageIndex = (page == null || page <= 1) ? 0 : Math.min(page,getLastPageNumber())-1 ;
        Pageable pageable = PageRequest.of( pageIndex , pageSize,
                Sort.by(Sort.Direction.DESC, "id"));
        return repository.getMachinesWithFilterAndPagination(id,machineNumber,pageable);
    }

    public Machine getOne(Long id){
        if(id == null) return new Machine();
        return repository.findById(id).orElse(null);
    }

    public void saveMachine(Machine machine, HttpServletRequest request,int objectCount) throws Exception{
        try {
            User user = seCommon.getUser(request);
            if(user == null) throw new Exception("Unauthorized Request");
            machine.setCreatedBy(user);
            int maxObjectCount = 600;
            String jsonMachine = new ObjectMapper().writeValueAsString(machine);
            if(objectCount<1) objectCount = 1; if(objectCount>maxObjectCount) objectCount = maxObjectCount;
            createBulkMachineProfile(jsonMachine,objectCount);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void createBulkMachineProfile(String jsonString,int objectCount) {
        new Thread(() -> {
            logger.info("Starting "+objectCount+" Machine Profile: "+jsonString);
            int count = 0;
            for(int i=0;i<objectCount;i++){
                try {
                    Machine machine = new ObjectMapper().readValue(jsonString,Machine.class);
                    machine = repository.save(machine);
                    machine.setMachineCode(generateMachineCode(machine.getId()));
                    repository.save(machine);
                    count++;
                }catch (Exception e){
                    System.out.println(jsonString);
                    System.out.println(e.getMessage());
                }
            }
            logger.info("Created "+count+" Machine Profile: "+jsonString);
        }).start();
    }

    public void updateMachine(Machine machine) throws Exception{
        try {
            logger.info("Before Machine Update: "+machine);
            Machine prvMachine = repository.findById(machine.getId()).orElse(null);
            if(prvMachine == null) throw  new Exception("No Machine Profile found for this ID: "+machine.getId());
            //Checking Duplicate Machine Serial Number
            if(!Objects.equals(prvMachine.getMachineNumber(), machine.getMachineNumber())){
                if(repository.getByMachineNumber(machine.getMachineNumber()).isPresent()){
                    throw new Exception("Machine already exist with this Machine Number "+machine.getMachineNumber());
                }
            }
            machine.setCreatedBy(prvMachine.getCreatedBy());
            machine.setCreationTime(prvMachine.getCreationTime());
            repository.save(machine);
            logger.info("Machine Update: "+machine);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Response updateMachineNumber(Long id,String machineNumber){
        Machine machine = repository.findById(id).orElse(null);
        if (machine==null) return new Response(false,"Machine Not Found");
        //Checking Duplicate Machine Serial Number
        if(!Objects.equals(machine.getMachineNumber(), machineNumber) &&
                repository.getByMachineNumber(machineNumber).isPresent())
            return new Response(false,"Machine Already Exist for this Machine Number "+machineNumber);
        machine.setMachineNumber(machineNumber);
        repository.save(machine);
        logger.info("Machine Number "+machineNumber+" Updated for Machine "+machine.getMachineCode());
        return new Response(true,"Machine Number Updated");
    }

    private String generateMachineCode(Long id){
        int codeLen = 6;
        StringBuilder sb = new StringBuilder();
        sb.append("CVM");
        for(int i=0;i<codeLen - Long.toString(id).length();i++){
            sb.append("0");
        }
        sb.append(id);
        return sb.toString();
    }
}
