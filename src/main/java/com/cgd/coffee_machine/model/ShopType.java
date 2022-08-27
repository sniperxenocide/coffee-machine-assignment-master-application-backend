package com.cgd.coffee_machine.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class ShopType  {
    @Id
    private Long id;
    private String name;
    private String description;

}
