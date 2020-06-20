package com.cantarino.app.demo.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class BankAccountRemovedEvent {

    private String id;
}
