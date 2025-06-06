package com.inigo.arch.domainevents

import com.inigo.arch.shared.domain.DomainEmitter
import com.inigo.arch.shared.domain.DomainEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class SharedEventEmitter() : DomainEmitter {
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Autowired
    fun setApplicationEventPublisher(applicationEventPublisher: ApplicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher
    }

    override fun <T : DomainEvent> emit(event: T) {
        applicationEventPublisher.publishEvent(event)
    }
}