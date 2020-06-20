package com.cantarino.app.demo.aggregates;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import java.math.BigDecimal;

import com.cantarino.app.demo.commands.AddBankAccountCommand;
import com.cantarino.app.demo.commands.RemoveBankAccountCommand;
import com.cantarino.app.demo.commands.UpdateBalanceBankAccountCommand;
import com.cantarino.app.demo.events.BankAccountAddedEvent;
import com.cantarino.app.demo.events.BankAccountBalanceUpdatedEvent;
import com.cantarino.app.demo.events.BankAccountRemovedEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;



@Slf4j
@Getter
@Aggregate
@NoArgsConstructor
public class BankAccountAggregate {

    @AggregateIdentifier
    private String id;
    private String name;
    private BigDecimal balance;



    @CommandHandler
    public BankAccountAggregate(AddBankAccountCommand command)
    {
        log.info("Handling {} command: {}", command.getClass().getSimpleName(), command);
        Assert.hasLength(command.getId(), "Id should not be empty or null.");
        Assert.hasLength(command.getName(), "Name should not be empty or null.");

        apply(new BankAccountAddedEvent(command.getId(), command.getName(), BigDecimal.ZERO));
        log.info("Done handling {} command: {}", command.getClass().getSimpleName(), command);
    }

    @CommandHandler
    public void handle(UpdateBalanceBankAccountCommand command) {
        log.info("Handling {} command: {}", command.getClass().getSimpleName(), command);
        Assert.hasLength(command.getBankId(), "Bank Id should not be empty or null.");
        Assert.notNull(command.getBalance(), "Balance should not be empty or null.");
        apply(new BankAccountBalanceUpdatedEvent(command.getBankId(), command.getBalance()));
        log.info("Done handling {} command: {}", command.getClass().getSimpleName(), command);
    }

    @CommandHandler
    public void handle(RemoveBankAccountCommand command) {
        log.info("Handling {} command: {}", command.getClass().getSimpleName(), command);
        Assert.hasLength(command.getId(), "Id should not be empty or null.");
        apply(new BankAccountRemovedEvent(command.getId()));
        log.info("Done handling {} command: {}", command.getClass().getSimpleName(), command);
    }

    @EventSourcingHandler
    public void on(BankAccountAddedEvent event) {
        log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);
        this.id = event.getId();
        this.name = event.getName();
        this.balance = event.getBalance();
        log.info("Done handling {} event: {}", event.getClass().getSimpleName(), event);
    }

    @EventSourcingHandler
    public void on(BankAccountBalanceUpdatedEvent event) {
        log.info("Handling {} event: {}", event.getClass().getSimpleName(), event);
        this.balance = event.getBalance();
        log.info("Done handling {} event: {}", event.getClass().getSimpleName(), event);
    }

    @EventSourcingHandler
    public void on(BankAccountRemovedEvent event) {
        log.info("Done handling {} event: {}", event.getClass().getSimpleName(), event);
    }
}
