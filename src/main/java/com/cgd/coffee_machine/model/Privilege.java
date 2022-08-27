package com.cgd.coffee_machine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor
@Entity
public class Privilege{
    @Id private Long id;
    private String name;
    private String api;
    private String method;
    private String description;

}
