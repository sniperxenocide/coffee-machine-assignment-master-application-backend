package com.cgd.coffee_machine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data @NoArgsConstructor @AllArgsConstructor
public class ContractDeleteDto {
    private String deleteReason;
    private LocalDateTime withdrawDate;
    private String comment;

    public void setWithdrawDate(String withdrawDate){
        if(withdrawDate==null || withdrawDate.isEmpty()) this.withdrawDate = null;
        else this.withdrawDate = LocalDateTime.parse(withdrawDate+"T00:00:00");
    }
}
