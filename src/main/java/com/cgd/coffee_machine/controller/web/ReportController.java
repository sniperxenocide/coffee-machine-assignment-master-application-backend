package com.cgd.coffee_machine.controller.web;

import com.cgd.coffee_machine.service.SeReport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReportController {
    private final SeReport service;

    public ReportController(SeReport service) {
        this.service = service;
    }

    @GetMapping("/report")
    public String reportPage(){
        return "report";
    }

    @GetMapping("/report/machine/summary")
    public String machineSummaryReport(Model model){
        model.addAttribute("summaryList",service.getMachineSummary());
        return "report-machine-summary";
    }

    @GetMapping("/report/shop/summary/location_wise")
    public String locationWiseShopSummaryReport(Model model){
        model.addAttribute("territoryWiseList",service.getTerritoryWiseShopSummary());
        model.addAttribute("regionWiseList",service.getRegionWiseShopSummary());
        model.addAttribute("divisionWiseList",service.getDivisionWiseShopSummary());
        return "report-shop-summary-location-wise";
    }

    @GetMapping("/report/shop/summary/type_wise")
    public String typeWiseShopSummaryReport(Model model){
        model.addAttribute("summaryList",service.getTypeWiseShopSummary());
        return "report-shop-summary-type-wise";
    }

    @GetMapping("/report/shop_machine_detail/location_wise")
    public String locationWiseShopMachineDetailReport(Model model,
                         @RequestParam(required = false) String territory){
        model.addAttribute("shopList",service.getShopDetailList(territory));
        model.addAttribute("marketHierarchy",service.getMarketHierarchyList());
        return "report-shop-machine-detail-location-wise";
    }
}
