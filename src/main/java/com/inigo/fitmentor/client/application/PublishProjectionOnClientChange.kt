package com.inigo.fitmentor.client.application

import com.inigo.fitmentor.client.infrastructure.ClientRepository
import com.inigo.shared.domain.events.ClientUpdated
import org.springframework.context.event.EventListener

class PublishProjectionOnClientUpdated(val repo: ClientRepository) {
    @EventListener
    fun on(event: ClientUpdated) {
        val client = repo.findById(event.id)
    }
}