package com.enset.comptemanagementcqrses.commonapi.query.controllers;

import com.enset.comptemanagementcqrses.commonapi.query.entities.Account;
import com.enset.comptemanagementcqrses.commonapi.query.queries.GetAccountByIdQuery;
import com.enset.comptemanagementcqrses.commonapi.query.queries.GetAllAccountQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/query/account")
@AllArgsConstructor
@Slf4j
public class AccountQueryController {
    private QueryGateway queryGateway;

    @GetMapping(path = "/allAccounts")
    public List<Account> accountList() {
        List<Account> response = queryGateway.query(
                new GetAllAccountQuery(),
                ResponseTypes.multipleInstancesOf(Account.class)).join();
        return response;
        //multipleInstancesOf --> pour une list
        //instanceOf --> un seul object account
    }

    @GetMapping(path = "/byId/{accountId}")
    public Account accountById(@PathVariable String accountId) {
         Account response = queryGateway.query(
                new GetAccountByIdQuery(accountId),
                ResponseTypes.instanceOf(Account.class)).join();
        return response;
        //multipleInstancesOf --> pour une list
        //instanceOf --> un seul object account
    }

}
