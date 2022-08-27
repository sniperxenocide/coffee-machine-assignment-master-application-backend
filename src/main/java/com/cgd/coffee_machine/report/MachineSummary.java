package com.cgd.coffee_machine.report;


import lombok.*;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class MachineSummary {
    private String brand;
    private String country;
    private String chamberOption;
    private Long total;
    private Long totalAssigned;
    private Long totalUnassigned;
}
