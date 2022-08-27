package com.cgd.coffee_machine.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class TypeWiseShopSummary {
    private String shopType;
    private Long totalShop;
    private Long totalMachine;
    private Long totalUnassignedShop;
}
