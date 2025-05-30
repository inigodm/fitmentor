package com.inigo.arch.shared.domain

import com.inigo.arch.domainevents.SharedEventEmitter
import java.util.ArrayList

abstract class AggregateRoot(
    val domainEmitter: DomainEmitter = SharedEventEmitter,
    val cache: MutableList<DomainEvent> = ArrayList<DomainEvent>()) {

    fun record(event: DomainEvent) {
        cache.add(event)
    }

    fun record(events: List<DomainEvent>) {
        cache.addAll(events)
    }
    fun publishEvents(eventPublisher: EventPublisher) {
        cache.stream()
            .forEach { event: DomainEvent -> eventPublisher.publish(event) }
    }
}