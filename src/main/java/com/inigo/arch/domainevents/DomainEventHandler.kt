package com.inigo.arch.domainevents

import com.inigo.arch.shared.domain.DomainEvent

interface DomainEventHandler<T : DomainEvent?> {
    fun handle(event: T?)
}
