package com.enset.comptemanagementcqrses.commands.aggregates;
import com.enset.comptemanagementcqrses.commonapi.commands.CreateAccountCommand;
import com.enset.comptemanagementcqrses.commonapi.commands.CreditAccountCommand;
import com.enset.comptemanagementcqrses.commonapi.commands.DebitAccountCommand;
import com.enset.comptemanagementcqrses.commonapi.enums.AccountStatus;
import com.enset.comptemanagementcqrses.commonapi.events.AccountActivatedEvent;
import com.enset.comptemanagementcqrses.commonapi.events.AccountCreatedEvent;
import com.enset.comptemanagementcqrses.commonapi.events.AccountCreditedEvent;
import com.enset.comptemanagementcqrses.commonapi.events.AccountDebitedEvent;
import com.enset.comptemanagementcqrses.commonapi.exceptions.AmountNegativeException;
import com.enset.comptemanagementcqrses.commonapi.exceptions.BalanceNotSufficientException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
        //obligatoire required by axon
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        if(createAccountCommand.getInitialBalance() < 0) new RuntimeException("Impossible ...");
        //ok
        //pour envoyer un evenement -> il faut le creer
        AggregateLifecycle.apply(new AccountCreatedEvent(
           createAccountCommand.getId(),
           createAccountCommand.getInitialBalance(),
           createAccountCommand.getCurrency(),
                AccountStatus.CREATED));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        this.accountId = event.getId();//obligatoire
        this.balance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
           event.getId(),
           AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent  event) {
        this.status = event.getStatus();

    }

    //********************** pour crediter
    //fonction de decision
    //pour excecuter la commande on utilise handle()
    @CommandHandler
    public void handle(CreditAccountCommand command) {
        if(command.getAmount() < 0 ) throw new AmountNegativeException("Amount shouldn't be Negative ....");
        AggregateLifecycle.apply(
          new AccountCreditedEvent(command.getId(), command.getAmount(), command.getCurrency())
        );
    }

    //la fonction d'evolution
    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        this.accountId = event.getId();//obligatoire
        this.balance += event.getAmount();
        this.currency = event.getCurrency();

    }

    //********************** pour Debiter
    //fonction de decision
    //pour excecuter la commande on utilise handle()
    @CommandHandler
    public void handle(DebitAccountCommand command) {
        if(command.getAmount() < 0 ) throw new AmountNegativeException("Amount shouldn't be Negative ....");
        if(this.balance < command.getAmount()) throw new BalanceNotSufficientException("Balance not sufficient Exception => " + balance);
        AggregateLifecycle.apply(
                new AccountDebitedEvent(command.getId(), command.getAmount(), command.getCurrency())
        );
    }

    //la fonction d'evolution
    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        this.accountId = event.getId();//obligatoire
        this.balance -= event.getAmount();
        this.currency = event.getCurrency();

    }
}
