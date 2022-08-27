package com.cgd.coffee_machine.report;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class LocationWiseShopSummary {
    private String division;
    private String region;
    private String territory;
    private Long totalShop;
    private Long totalMachine;
    private Long totalUnassignedShop;
}
