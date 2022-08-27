package com.cgd.coffee_machine.request_response;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class MarketHierarchy {
    private String division;
    private String region;
    private String territory;
}
