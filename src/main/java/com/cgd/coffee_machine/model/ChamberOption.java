package com.cgd.coffee_machine.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data @ToString
@Entity
public class ChamberOption {

    @Id private Long id;

    private String name;

    private String description;

}
