package com.inigo.fitmentor.client.application

import com.inigo.arch.shared.domain.errors.SnapshotNotSendError
import com.inigo.fitmentor.client.infrastructure.ClientRepository
import com.inigo.fitmentor.shared.domain.events.ClientUpdated
import com.inigo.arch.kafka.KafkaProducerService
import org.springframework.context.event.EventListener

class ProjectOnClientUpdated(val repo: ClientRepository, val kafkaProducerService: KafkaProducerService) {
    @EventListener
    fun on(event: ClientUpdated) {
        repo.findById(event.clientId)
            .map { clientJpa ->  clientJpa.toDomain() }
            .map { kafkaProducerService.sendSnapshot(it) }
            .orElseThrow { SnapshotNotSendError.becauseNoClientExistsForGivenId(event.id.toString()) }
    }
}