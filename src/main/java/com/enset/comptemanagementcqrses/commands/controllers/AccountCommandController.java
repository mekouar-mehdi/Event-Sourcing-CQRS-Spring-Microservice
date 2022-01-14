package com.enset.comptemanagementcqrses.commands.controllers;

import com.enset.comptemanagementcqrses.commonapi.commands.CreateAccountCommand;
import com.enset.comptemanagementcqrses.commonapi.commands.CreditAccountCommand;
import com.enset.comptemanagementcqrses.commonapi.commands.DebitAccountCommand;
import com.enset.comptemanagementcqrses.commonapi.dtos.CreateAccountDTO;
import com.enset.comptemanagementcqrses.commonapi.dtos.CreditAccountDTO;
import com.enset.comptemanagementcqrses.commonapi.dtos.DebitAccountDTO;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping(path="/commands/account")
@AllArgsConstructor
public class AccountCommandController {

    private CommandGateway commandGateway;
    private EventStore eventStore;

    @PostMapping(path="/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountDTO request) {
        CompletableFuture<String> commandResponse = commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                request.getInitialBalance(),
                request.getCurrency()
        ));
        return commandResponse;
    }


    @PutMapping(path="/credit")
    public CompletableFuture<String> creditAccount(
            @RequestBody CreditAccountDTO request
    ) {
        CompletableFuture<String> commandResponse = commandGateway.send(
                new CreditAccountCommand(request.getAccountId(), request.getAmount(), request.getCurrency())
        );
        return commandResponse;
    }

    @PutMapping(path="/debit")
    public CompletableFuture<String> debitAccount(
            @RequestBody DebitAccountDTO request
    ) {
        CompletableFuture<String> commandResponse = commandGateway.send(
                new DebitAccountCommand(request.getAccountId(), request.getAmount(), request.getCurrency())
        );
        return commandResponse;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/eventStore/{accountId}")
    public Stream eventStore(@PathVariable String accountId) {
        return eventStore.readEvents(accountId).asStream();
    }


}
