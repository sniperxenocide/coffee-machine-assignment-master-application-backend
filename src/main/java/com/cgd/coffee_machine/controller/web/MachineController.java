package com.cgd.coffee_machine.controller.web;

import com.cgd.coffee_machine.CgdCoffeeMachineModule;
import com.cgd.coffee_machine.model.Machine;
import com.cgd.coffee_machine.service.SeChamberOption;
import com.cgd.coffee_machine.service.SeMachine;
import com.cgd.coffee_machine.service.SeMachineBrand;
import com.cgd.coffee_machine.service.SeOriginCountry;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class MachineController {

    private final SeMachine service;
    private final SeMachineBrand seMachineBrand;
    private final SeChamberOption seChamberOption;
    private final SeOriginCountry seOriginCountry;

    public MachineController(SeMachine service, SeMachineBrand seMachineBrand, SeChamberOption seChamberOption, SeOriginCountry seOriginCountry) {
        this.service = service;
        this.seMachineBrand = seMachineBrand;
        this.seChamberOption = seChamberOption;
        this.seOriginCountry = seOriginCountry;
    }

    @GetMapping("/machine")
    public String getAllMachines(Model model,@RequestParam(required = false) Integer page,
                @RequestParam(required = false,defaultValue = "%") String id,
                @RequestParam(required = false,defaultValue = "%") String machineNumber){

        Page<Machine> list = service.getMachines(id,machineNumber,page);
        model.addAttribute("machines", list);
        model.addAttribute("page",(list.getPageable().getPageNumber()+1));
        model.addAttribute("lastPage",service.getLastPageNumber());
        model.addAttribute("totalMachineCount", service.getMachineCount());

        return "machine";
    }

    //@GetMapping("/machine")
//    public String machinePage(Model model,@RequestParam(required = false) Integer page,
//                              @RequestParam(required = false) Long id){
//
//        if (id==null) {
//            Page<Machine> list = service.getAllPageable(page);
//            model.addAttribute("machines", list);
//            model.addAttribute("page",(list.getPageable().getPageNumber()+1));
//            model.addAttribute("lastPage",service.getLastPageNumber());
//            model.addAttribute("totalMachineCount", service.getMachineCount());
//        }
//        else {
//            ArrayList<Machine> list = new ArrayList<>();
//            Machine m = service.getOne(id);
//            if(m!=null)list.add(m);
//            model.addAttribute("machines",list);
//        }
//        return "machine";
//    }


    @GetMapping(value = {"/machine/add"})
    public String addMachinePage(Model model) {
        addAttrs(model,true,service.getOne(null),null);
        return "machine-add-edit";
    }

    @PostMapping("/machine/create")
    public String createMachine(Model model, @ModelAttribute("machine") Machine machine,
                                @RequestParam int cnt,HttpServletRequest request){
        try {
            service.saveMachine(machine,request,cnt);
            model.addAttribute("msg",cnt+" Machine Profile Creation Process Started in Background and will be Finished Shortly.");
            return "response";
        }catch (Exception e){
            addAttrs(model,true,null,e);
            return "machine-add-edit";
        }
    }

    @GetMapping(value = {"/machine/edit"})
    public String editMachinePage(Model model, @RequestParam Long id) {
        Machine machine = service.getOne(id);
        if(machine==null) {
            model.addAttribute("msg","No Machine found with this ID: "+id);
            return "response";
        }
        model.addAttribute("contractHistory",service.getContractHistoryOfMachine(machine.getId()));

        addAttrs(model,false,machine,null);
        return "machine-add-edit";
    }

    @PostMapping("/machine/update")
    public String updateMachine(Model model, @ModelAttribute("machine") Machine machine){
        try {
            service.updateMachine(machine);
            model.addAttribute("msg","Machine Profile Updated");
            return "response";
        }catch (Exception e){
            addAttrs(model,false,null,e);
            return "machine-add-edit";
        }
    }

    public void addAttrs(Model model, boolean add, Machine machine, Exception e){
        model.addAttribute("add", add);
        if(e != null) model.addAttribute("errorMessage", e.getMessage());
        if(machine!=null) model.addAttribute("machine", machine);
        model.addAttribute("brands",seMachineBrand.getAll());
        model.addAttribute("options",seChamberOption.getAll());
        model.addAttribute("origins",seOriginCountry.getAll());
    }
}
