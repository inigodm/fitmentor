package com.inigo.arch.shared.domain;

public interface EventPublisher {

    void publish(DomainEvent event);
}