package com.cgd.coffee_machine.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
@Entity
@Table(name = "oracle_distributor")
public class OracleDistributor {
    @Id @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonProperty("customerNumber")
    @Column(name = "oracle_code", nullable = false, unique = true, length = 20)
    private String oracleCode;

    @JsonProperty("customerName")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "division", nullable = false)
    private String division;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "territory", nullable = false)
    private String territory;

}