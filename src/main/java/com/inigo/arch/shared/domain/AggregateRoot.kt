package com.inigo.arch.shared.domain

import com.inigo.arch.domainevents.SharedEventEmitter
import com.inigo.arch.shared.infrastructure.SpringContext
import java.util.ArrayList

abstract class AggregateRoot() {
    private val domainEmitter: DomainEmitter = SpringContext.getBean(SharedEventEmitter::class.java)
    private val cache: MutableList<DomainEvent> = ArrayList()

    fun record(event: DomainEvent) {
        cache.add(event)
    }

    fun record(events: List<DomainEvent>) {
        cache.addAll(events)
    }

    fun publishEvents() {
        cache.forEach { event -> domainEmitter.emit(event) }
    }
}