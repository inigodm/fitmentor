package com.inigo.arch.shared.domain

import com.inigo.arch.domainevents.SharedEventEmitter
import com.inigo.arch.shared.infrastructure.SpringContext
import java.util.ArrayList
import java.util.UUID

abstract class AggregateRoot(val uuid : UUID = UUID.randomUUID(), val aggregateName: String) {
    private val domainEmitter: DomainEmitter = SpringContext.getBean(SharedEventEmitter::class.java)
    private val cache: MutableList<DomainEvent> = ArrayList()

    fun record(event: DomainEvent) {
        cache.add(event)
    }

    fun publishEvents() {
        println("Publishing events")
        cache.forEach { event ->
                println("Emitting event: $event")
                domainEmitter.emit(event)
                println("Event: $event")

        }
        cache.clear()
    }
}