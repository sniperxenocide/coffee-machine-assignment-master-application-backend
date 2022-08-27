package com.cgd.coffee_machine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor
@Entity
public class Role {
    @Id private Long id;
    private String name;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "role_privilege",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private List<Privilege> privileges;
}
