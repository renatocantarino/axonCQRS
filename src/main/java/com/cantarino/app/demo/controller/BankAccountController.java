package com.cantarino.app.demo.controller;


import com.cantarino.app.demo.Dtos.BankAccountDTO;
import com.cantarino.app.demo.commands.AddBankAccountCommand;
import com.cantarino.app.demo.commands.RemoveBankAccountCommand;
import com.cantarino.app.demo.commands.UpdateBalanceBankAccountCommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Slf4j
@RestController
@RequestMapping("/api/bank-accounts")
public class BankAccountController {



    private final CommandGateway commandGateway;

    public BankAccountController(CommandGateway commandGateway) {
        this.commandGateway=commandGateway;
    }




    @PostMapping
    public CompletableFuture<String> create(@RequestBody final BankAccountDTO dto) {

        final AddBankAccountCommand command = new AddBankAccountCommand(UUID.randomUUID().toString(), dto.getName());
        log.info("Executing command: {}", command);
        return commandGateway.send(command);

    }

    @PutMapping("/{id}/balances")
    public CompletableFuture<String> updateBalance(@PathVariable final String id, @RequestBody final BankAccountDTO dto) {
        final UpdateBalanceBankAccountCommand command = new UpdateBalanceBankAccountCommand(id, dto.getBalance());
        log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<String> remove(@PathVariable final String id) {
        final RemoveBankAccountCommand command = new RemoveBankAccountCommand(id);
        log.info("Executing command: {}", command);
        return commandGateway.send(command);
    }
}
