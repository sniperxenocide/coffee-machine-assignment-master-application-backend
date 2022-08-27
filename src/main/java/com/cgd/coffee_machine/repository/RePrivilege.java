package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RePrivilege extends JpaRepository<Privilege,Long> {
    Optional<Privilege> findByName(String name);
    Optional<Privilege> findByApi(String api);
}
