package com.cgd.coffee_machine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "contract_history")
public class ContractHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "machine_id")
    private Long machineId;

    @Column(name = "machine_number")
    private String machineNumber;

    @Column(name = "machine_model")
    private String machineModel;

    @Column(name = "machine_brand")
    private String machineBrand;

    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "shop_code")
    private String shopCode;

    @Column(name = "shop_address")
    private String shopAddress;

    @Column(name = "shop_owner_name")
    private String shopOwnerName;

    @Column(name = "shop_owner_phone")
    private String shopOwnerPhone;

    @Column(name = "division")
    private String division;

    @Column(name = "region")
    private String region;

    @Column(name = "territory")
    private String territory;

    @Column(name = "mso_name")
    private String msoName;

    @Column(name = "maos_phone")
    private String msoPhone;

    private Double machineAllotmentPrice;

    private Double downPayment;

    @Column(name = "installment_type_id")
    private Long installmentTypeId;

    @Column(name = "installment_type")
    private String installmentType;

    private Integer numberOfInstallment;

    private Double perInstallmentAmount;

    private Double dailyTargetVolumeKg;

    private LocalDate handOverDate;

    private Integer serviceWarrantyMonth;

    private Double securityMoney;

    private LocalDate securityMoneyReturnDate;

    private LocalDate installmentStartDate;

    @Column(name = "payment_term_id")
    private Long paymentTermId;

    @Column(name = "payment_term")
    private String paymentTerm;

    @ManyToOne
    @JoinColumn(name = "contract_created_by",referencedColumnName = "id")
    private User contractCreatedBy;

    @Column(name = "contract_creation_time")
    private LocalDateTime contractCreationTime;

    @Column(name = "contract_end_time")
    private LocalDateTime contractEndTime;

    @Column(name = "change_reason",length = 500)
    private String changeReason;

    @ManyToOne
    @JoinColumn(name = "created_by",referencedColumnName = "id")
    private User createdBy;

    @CreationTimestamp
    @Column(name = "creation_time",columnDefinition = "datetime default current_timestamp ")
    private LocalDateTime creationTime;
    
    public ContractHistory(Contract previousContract,User user,String changeReason){
        this.contractId = previousContract.getId();

        this.shopId = previousContract.getShop().getId();
        this.shopName = previousContract.getShop().getShopName();
        this.shopAddress = previousContract.getShop().getAddress();
        this.shopCode = previousContract.getShop().getShopCode();
        this.shopOwnerName = previousContract.getShop().getProprietorName();
        this.shopOwnerPhone = previousContract.getShop().getProprietorPhone();
        this.division = previousContract.getShop().getDivision();
        this.region = previousContract.getShop().getRegion();
        this.territory = previousContract.getShop().getTerritory();
        this.msoName = previousContract.getShop().getMso().getName();
        this.msoPhone = previousContract.getShop().getMso().getPhone();

        this.machineId = previousContract.getMachine().getId();
        this.machineNumber = previousContract.getMachine().getMachineNumber();
        this.machineModel = previousContract.getMachine().getModelNumber();
        this.machineBrand = previousContract.getMachine().getMachineBrand().getName();

        this.createdBy = user;
        this.changeReason = changeReason;

        this.machineAllotmentPrice = previousContract.getMachineAllotmentPrice();
        this.downPayment = previousContract.getDownPayment();
        this.installmentTypeId = previousContract.getInstallmentType().getId();
        this.installmentType = previousContract.getInstallmentType().getName();
        this.numberOfInstallment = previousContract.getNumberOfInstallment();
        this.perInstallmentAmount = previousContract.getPerInstallmentAmount();
        this.dailyTargetVolumeKg = previousContract.getDailyTargetVolumeKg();
        this.setHandoverDate(previousContract.getHandoverDate());
        this.serviceWarrantyMonth = previousContract.getServiceWarrantyMonth();
        this.securityMoney = previousContract.getSecurityMoney();
        this.setSecurityMoneyReturnDate(previousContract.getSecurityMoneyReturnDate());
        this.setInstallmentStartDate(previousContract.getInstallmentStartDate());
        this.paymentTermId = previousContract.getPaymentTerm().getId();
        this.paymentTerm = previousContract.getPaymentTerm().getName();
        this.contractCreatedBy = previousContract.getCreatedBy();
        this.contractCreationTime = previousContract.getCreationTime();
//        this.contractEndTime = LocalDateTime.now();
    }


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