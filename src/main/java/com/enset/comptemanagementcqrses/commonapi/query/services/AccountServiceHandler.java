package com.enset.comptemanagementcqrses.commonapi.query.services;

import com.enset.comptemanagementcqrses.commonapi.enums.OperationType;
import com.enset.comptemanagementcqrses.commonapi.events.AccountActivatedEvent;
import com.enset.comptemanagementcqrses.commonapi.events.AccountCreatedEvent;
import com.enset.comptemanagementcqrses.commonapi.events.AccountCreditedEvent;
import com.enset.comptemanagementcqrses.commonapi.events.AccountDebitedEvent;
import com.enset.comptemanagementcqrses.commonapi.query.entities.Account;
import com.enset.comptemanagementcqrses.commonapi.query.entities.Operation;
import com.enset.comptemanagementcqrses.commonapi.query.queries.GetAccountByIdQuery;
import com.enset.comptemanagementcqrses.commonapi.query.queries.GetAllAccountQuery;
import com.enset.comptemanagementcqrses.commonapi.query.repositories.AccountRepository;
import com.enset.comptemanagementcqrses.commonapi.query.repositories.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountServiceHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    @EventHandler
    public void on(AccountCreatedEvent event){
        //ici il n'ya pas de logique metier par ce que la logique metier est dans l'aggregat
        log.info("**********************************");
        log.info("AccountCreatedEvent received *****");
        Account account = new Account();
        account.setId(event.getId());
        account.setBalance(event.getInitialBalance());
        account.setCurrency(event.getCurrency());
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent event){
        //ici il n'ya pas de logique metier par ce que la logique metier est dans l'aggregat
        log.info("**********************************");
        log.info("AccountCreatedEvent received *****");
        Account account = accountRepository.findById(event.getId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }


    @EventHandler
    public void on(AccountCreditedEvent event){
        //ici il n'ya pas de logique metier par ce que la logique metier est dans l'aggregat
        log.info("**********************************");
        log.info("AccountCreditedEvent received *****");
        Account account = accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());
        operation.setType(OperationType.CREDIT);
        operationRepository.save(operation);
        //mise a jour du compte
        account.setBalance(account.getBalance()+event.getAmount());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent event){
        //ici il n'ya pas de logique metier par ce que la logique metier est dans l'aggregat
        log.info("**********************************");
        log.info("AccountDebitedEvent received *****");
        Account account = accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());
        operation.setType(OperationType.DEBIT);
        operationRepository.save(operation);
        //mise a jour du compte
        account.setBalance(account.getBalance()-event.getAmount());
        accountRepository.save(account);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountQuery getAllAccountQuery) {
        return accountRepository.findAll();
    }

    @QueryHandler
    public Account on(GetAccountByIdQuery getAccountQuery) {
        return accountRepository.findById(getAccountQuery.getId()).get();
    }


}
