package com.enset.comptemanagementcqrses.commonapi.events;

import com.enset.comptemanagementcqrses.commonapi.enums.AccountStatus;
import lombok.Getter;

public class AccountCreatedEvent extends BaseEvent<String> {
    //ce qu'on va garder dans le event store
    @Getter
    private double initialBalance;

    @Getter
    private String currency;

    @Getter
    private AccountStatus status;

    public AccountCreatedEvent(String id, double initialBalance, String currency, AccountStatus status) {
        super(id);
        this.initialBalance = initialBalance;
        this.currency = currency;
        this.status = status;
    }
}
