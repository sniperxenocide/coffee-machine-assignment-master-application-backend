package com.cgd.coffee_machine.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Contract  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    private Double machineAllotmentPrice;
    private Double downPayment;
    @ManyToOne
    @JoinColumn(name = "installment_type_id")
    private InstallmentType installmentType;
    private Integer numberOfInstallment;
    private Double perInstallmentAmount;
    private Double dailyTargetVolumeKg;
    private LocalDate handOverDate;
    private Integer serviceWarrantyMonth;
    private Double securityMoney;
    private LocalDate securityMoneyReturnDate;
    private LocalDate installmentStartDate;
    @ManyToOne
    @JoinColumn(name = "payment_term_id")
    private PaymentTerm paymentTerm;
    @ManyToOne
    @JoinColumn(name = "created_by",referencedColumnName = "id")
    private User createdBy;
    @CreationTimestamp
    private LocalDateTime creationTime;


    public void setHandoverDate(String handOverDate){
        this.handOverDate = LocalDate.parse(handOverDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getHandoverDate(){
        if(this.handOverDate == null) return "";
        return this.handOverDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void setSecurityMoneyReturnDate(String securityMoneyReturnDate){
        try {
            this.securityMoneyReturnDate = LocalDate
                    .parse(securityMoneyReturnDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch (Exception ignored){
            this.securityMoneyReturnDate = null;
        }
    }

    public String getSecurityMoneyReturnDate(){
        if(this.securityMoneyReturnDate == null) return "";
        return this.securityMoneyReturnDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void setInstallmentStartDate(String installmentStartDate){
        try {
            this.installmentStartDate = LocalDate.
                    parse(installmentStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch (Exception ignored) {this.installmentStartDate = null;}
    }

    public String getInstallmentStartDate(){
        if(this.installmentStartDate == null) return "";
        return this.installmentStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
