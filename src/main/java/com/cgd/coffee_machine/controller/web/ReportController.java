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
    public String machineSummaryReport(
            Model model,
            @RequestParam(required = false,defaultValue = "1970-01-01") String startDate,
            @RequestParam(required = false,defaultValue = "2100-12-31") String endDate){
        model.addAttribute("summaryList",service.getMachineSummary(startDate, endDate));
        model.addAttribute("startDate",startDate.startsWith("20")?startDate:"");
        model.addAttribute("endDate",endDate.startsWith("20")?endDate:"");
        return "report-machine-summary";
    }

    @GetMapping("/report/shop/summary/location_wise")
    public String locationWiseShopSummaryReport(
            Model model,
            @RequestParam(required = false,defaultValue = "1970-01-01") String startDate,
            @RequestParam(required = false,defaultValue = "2100-12-31") String endDate){
        model.addAttribute("territoryWiseList",service.getTerritoryWiseShopSummary(startDate, endDate));
        model.addAttribute("startDate",startDate.startsWith("20")?startDate:"");
        model.addAttribute("endDate",endDate.startsWith("20")?endDate:"");
        return "report-shop-summary-location-wise";
    }

    @GetMapping("/report/shop/summary/type_wise")
    public String typeWiseShopSummaryReport(Model model){
        model.addAttribute("summaryList",service.getTypeWiseShopSummary());
        return "report-shop-summary-type-wise";
    }

    @GetMapping("/report/shop_machine_detail/location_wise")
    public String locationWiseShopMachineDetailReport(Model model,
                         @RequestParam(required = false,defaultValue = "") String division,
                         @RequestParam(required = false,defaultValue = "") String region,
                         @RequestParam(required = false,defaultValue = "") String territory){
        model.addAttribute("shopList",service.getShopDetailList(division,region,territory));
        model.addAttribute("marketHierarchy",service.getMarketHierarchyList());
        model.addAttribute("division",division);
        model.addAttribute("region",region);
        model.addAttribute("territory",territory);
        return "report-shop-machine-detail-location-wise";
    }
}
