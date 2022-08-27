package com.cgd.coffee_machine.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @ToString
@Entity
@Table(name = "payment_term")
public class PaymentTerm  {
    @Id private Long id;
    private String name;
    private Double machineAllotmentPrice;
    private Double downPayment;
    @Column(name = "installment_type_id")
    private Long installmentType;
    private Integer numberOfInstallment;
    private Double perInstallmentAmount;

}
