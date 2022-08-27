package com.cgd.coffee_machine.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class InstallmentType {

    @Id private Long id;
    private String name;
    private String description;

}
