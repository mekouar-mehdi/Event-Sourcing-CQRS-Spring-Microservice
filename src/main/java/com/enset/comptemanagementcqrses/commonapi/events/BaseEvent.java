package com.enset.comptemanagementcqrses.commonapi.events;

import lombok.Getter;

public abstract class BaseEvent<T> {
    @Getter
    public T id;

    protected BaseEvent(T id) {
        this.id = id;
    }
}
