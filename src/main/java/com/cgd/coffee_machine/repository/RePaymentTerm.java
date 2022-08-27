package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.PaymentTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RePaymentTerm extends JpaRepository<PaymentTerm, Long>, JpaSpecificationExecutor<PaymentTerm> {

}