package com.cgd.coffee_machine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "shop")
public class Shop {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String shopName;
    private String shopCode;
    @ManyToOne
    @JoinColumn(name = "shop_type_id")
    private ShopType shopType;
    private String proprietorName;
    private String proprietorPhone;
    @ManyToOne
    @JoinColumn(name = "shop_grade_id")
    private ShopGrade shopGrade;
    private String proprietorNid;
    private String address;
    private String division;
    private String region;
    private String territory;
    @Column(name = "tso_name",length = 100)
    private String tsoName;
    @Column(name = "tso_phone",length = 50)
    private String tsoPhone;
    private String distributorName;
    @Column(name = "distributor_oracle_code",length = 50)
    private String distributorOracleCode;
    @ManyToOne
    @JoinColumn(name = "created_by",referencedColumnName = "id")
    private User createdBy;
    @CreationTimestamp
    private LocalDateTime creationTime;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "shop")
    private List<Contract> contracts;

    @JsonIgnore @ToString.Exclude
    @ManyToOne @JoinColumn(name = "responsible_officer_id")
    private ResponsibleOfficer mso;

}
