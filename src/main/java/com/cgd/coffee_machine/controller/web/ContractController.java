package com.cgd.coffee_machine.controller.web;

import com.cgd.coffee_machine.model.Contract;
import com.cgd.coffee_machine.model.Machine;
import com.cgd.coffee_machine.model.Shop;
import com.cgd.coffee_machine.service.SeContract;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ContractController {
    private final SeContract service;

    public ContractController(SeContract service) {
        this.service = service;
    }

    @GetMapping(value = {"/contract/add"})
    public String newContractPage(Model model,@RequestParam Long shopId,
                                  @RequestParam(required = false) Long machineId) {
        Contract contract = service.getOne(null);
        Shop shop = null;  Machine machine = null;  Exception exception = null;
        try {
            try {
                shop = service.getShop(shopId);
            }catch (Exception e){
                model.addAttribute("msg",e.getMessage());
                model.addAttribute("redirect","/shop");
                return "response";
            }
            if(machineId!=null) machine = service.getMachineWithNoContract(machineId);
        }catch (Exception e){ exception = e;}
        addAttrs(model,true,contract,shop,machine,exception);
        return "contract-add-edit";
    }

    @PostMapping("/contract/create")
    public String createContract(Model model, @ModelAttribute("contract") Contract contract, HttpServletRequest request,
                                 @RequestParam(required = false) String machineNumber){
        try {
            service.saveContract(contract,request,machineNumber);
            model.addAttribute("msg",
                    "Contract Created. Machine: "+contract.getMachine().getMachineCode()
            +" Assigned to Shop: "+contract.getShop().getShopCode());
            model.addAttribute("redirect","/shop");
            return "response";
        }catch (Exception e){
            service.logger.info(e.getMessage());
            addAttrs(model,true,null,contract.getShop(),contract.getMachine(),e);
            return "contract-add-edit";
        }
    }

    @GetMapping(value = {"/contract/edit"})
    public String editContractPage(Model model, @RequestParam Long id,
                               @RequestParam(required = false) Long machineId) {
        Contract contract = null;
        Shop shop = null;  Machine machine = null;  Exception exception = null;
        try {
            contract = service.getOne(id);
            if(contract==null) throw new Exception("Contract Doesn't Exist");
            shop = contract.getShop();
            machine = machineId==null ? contract.getMachine():service.getMachineWithNoContract(machineId);
        }catch (Exception e){ exception = e; e.printStackTrace();}
        addAttrs(model,false,contract,shop,machine,exception);
        return "contract-add-edit";
    }

    @PostMapping("/contract/update")
    public String updateContract(Model model, @ModelAttribute("contract") Contract contract){
        try {
            service.updateContract(contract);
            model.addAttribute("msg",
                    "Contract Updated. Machine: "+contract.getMachine().getMachineCode()
                            +" Assigned to Shop: "+contract.getShop().getShopCode());
            model.addAttribute("redirect","/shop");
            return "response";
        }catch (Exception e){
            service.logger.info(e.getMessage());
            addAttrs(model,false,null,contract.getShop(),contract.getMachine(),e);
            return "contract-add-edit";
        }
    }

    @PostMapping("/contract/delete")
    public String deleteContract(Model model,@RequestParam Long id) {
        Contract contract = null;Shop shop=null;Machine machine=null;
        try {
            contract = service.getOne(id);
            if(contract==null) throw new Exception("Contract Doesn't Exist");
            shop=contract.getShop(); machine=contract.getMachine();
            service.deleteContract(contract);
            model.addAttribute("msg","Contract ID: "+id+" Deleted.");
            model.addAttribute("redirect","/shop");
            return "response";
        }catch (Exception e){
            addAttrs(model,false,null,shop,machine,e);
            return "contract-add-edit";
        }
    }

    public void addAttrs(Model model, boolean add, Contract contract, Shop shop, Machine machine, Exception e){
        model.addAttribute("add", add);
        if(e != null) model.addAttribute("errorMessage", e.getMessage());
        if(contract!=null) model.addAttribute("contract", contract);
        if(shop!=null) model.addAttribute("shop", shop);
        if(machine!=null) model.addAttribute("machine", machine);
        if(add) model.addAttribute("availableMachines",service.getAvailableMachines());
        model.addAttribute("installment_types",service.getAllInstallmentType());
        model.addAttribute("payment_terms",service.getAllPaymentTerm());
    }
}
