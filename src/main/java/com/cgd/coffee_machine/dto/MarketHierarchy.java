package com.cgd.coffee_machine.dto;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class MarketHierarchy {
    private String division;
    private String region;
    private String territory;
}
