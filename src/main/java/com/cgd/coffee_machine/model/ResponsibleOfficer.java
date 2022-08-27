package com.cgd.coffee_machine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity @Getter @Setter @NoArgsConstructor @ToString
@Table(name = "responsible_officer")
public class ResponsibleOfficer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "division", length = 100)
    private String division;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "territory", length = 100)
    private String territory;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "phone", nullable = false, unique = true, length = 100)
    private String phone;

}