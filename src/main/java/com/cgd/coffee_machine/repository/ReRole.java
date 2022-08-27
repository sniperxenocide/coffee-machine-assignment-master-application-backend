package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReRole extends JpaRepository<Role,Long> {

}
