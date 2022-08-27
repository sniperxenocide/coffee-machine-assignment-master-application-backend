package com.cgd.coffee_machine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Machine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String machineNumber;
    private String machineCode;
    @ManyToOne
    @JoinColumn(name = "origin_id")
    private OriginCountry originCountry;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private MachineBrand machineBrand;
    @ManyToOne
    @JoinColumn(name = "option_id")
    private ChamberOption chamberOption;
    private String supplierName;
    private String supplierOracleCode;
    private LocalDate machineReceivingDate;
    private Integer warrantyPeriodMonth;
    private Double machinePoPrice;
    private Double machineLandedCost;
    private String poNumber;
    private String lcNumber;
    private String lotNumber;
    private String modelNumber;
    private String oracleItemCode;
    @ManyToOne
    @JoinColumn(name = "created_by",referencedColumnName = "id")
    private User createdBy;
    @CreationTimestamp
    private LocalDateTime creationTime;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "machine")
    private Contract contract;


    public void setMachineReceivingDate(String machineValidity){
        this.machineReceivingDate = LocalDate.parse(machineValidity, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getMachineReceivingDate(){
        if(this.machineReceivingDate == null) return "";
        return this.machineReceivingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


}
