package com.enset.comptemanagementcqrses.commonapi.dtos;

import lombok.Data;

@Data
public class DebitAccountDTO {
    private String accountId;
    private double amount;
    private String currency;
}
