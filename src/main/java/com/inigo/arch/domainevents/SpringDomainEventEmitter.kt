package com.inigo.arch.domainevents

import com.inigo.arch.shared.domain.DomainEmitter
import com.inigo.arch.shared.domain.DomainEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component


@Component
class SpringDomainEventEmitter(val applicationEventPublisher: ApplicationEventPublisher): DomainEmitter {

    override fun <T : DomainEvent> emit(event: T) {
        applicationEventPublisher.publishEvent(event)
    }
}
