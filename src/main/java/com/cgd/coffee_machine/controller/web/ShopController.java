package com.cgd.coffee_machine.controller.web;

import com.cgd.coffee_machine.model.Machine;
import com.cgd.coffee_machine.model.Shop;
import com.cgd.coffee_machine.service.SeShop;
import com.cgd.coffee_machine.service.SeShopGrade;
import com.cgd.coffee_machine.service.SeShopType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class ShopController {
    private final SeShop service;
    private final SeShopType seShopType;
    private final SeShopGrade seShopGrade;

    public ShopController(SeShop service, SeShopType seShopType, SeShopGrade seShopGrade) {
        this.service = service;
        this.seShopType = seShopType;
        this.seShopGrade = seShopGrade;
    }

    @GetMapping("/shop")
    public String shopPage(Model model,@RequestParam(required = false) Integer page,
                           @RequestParam(required = false) String code){
        if (code==null) {
            Page<Shop> list = service.getAllPageable(page);
            model.addAttribute("shops", list);
            model.addAttribute("page",(list.getPageable().getPageNumber()+1));
            model.addAttribute("lastPage",service.getLastPageNumber());
            model.addAttribute("totalShopCount", service.getShopCount());
        }
        else {
            ArrayList<Shop> list = new ArrayList<>();
            Shop s = service.getOneByShopCode(code);
            if(s!=null)list.add(s);
            model.addAttribute("shops",list);
        }
        return "shop";
    }

    @GetMapping(value = {"/shop/add"})
    public String addShopPage(Model model) {
        addAttrs(model,true,service.getOne(null),null);
        return "shop-add-edit";
    }

    @PostMapping("/shop/create")
    public String createShop(Model model, @ModelAttribute("shop") Shop shop, HttpServletRequest request){
        try {
            service.saveShop(shop,request);
            model.addAttribute("msg","Shop Profile Created with Code: "+shop.getShopCode());
            return "response";
        }catch (Exception e){
            addAttrs(model,true,null,e);
            return "shop-add-edit";
        }
    }

    @GetMapping(value = {"/shop/edit"})
    public String editShopPage(Model model, @RequestParam Long id) {
        Shop shop = service.getOne(id);
        if(shop==null) {
            model.addAttribute("msg","No Shop found with this ID: "+id);
            return "response";
        }
        addAttrs(model,false,shop,null);
        return "shop-add-edit";
    }

    @PostMapping("/shop/update")
    public String updateShop(Model model, @ModelAttribute("shop") Shop shop, HttpServletRequest request){
        try {
            service.updateShop(shop);
            model.addAttribute("msg","Shop Profile Updated");
            return "response";
        }catch (Exception e){
            addAttrs(model,false,null,e);
            return "shop-add-edit";
        }
    }

    public void addAttrs(Model model,boolean add,Shop shop,Exception e){
        model.addAttribute("add", add);
        if(e != null) model.addAttribute("errorMessage", e.getMessage());
        if(shop!=null) model.addAttribute("shop", shop);
        model.addAttribute("types",seShopType.getAll());
        model.addAttribute("grades",seShopGrade.getAll());
        model.addAttribute("msoList",service.getAllMso());
    }
}
