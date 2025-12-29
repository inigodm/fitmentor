package com.inigo.fitmentor.client.infrastructure

import com.inigo.arch.shared.domain.errors.SnapshotNotSendError
import com.inigo.fitmentor.shared.domain.events.ClientUpdated
import com.inigo.arch.kafka.KafkaProducerService
import org.springframework.context.event.EventListener

class DoProjectionOnClientUpdated(val repo: ClientJpaRepository, val kafkaProducerService: KafkaProducerService) {
    @EventListener
    fun on(event: ClientUpdated) {
        repo.findById(event.clientId)
            .map { clientJpa ->  clientJpa.toDomain() }
            .map { kafkaProducerService.sendSnapshot(it) }
            .orElseThrow { SnapshotNotSendError.becauseNoClientExistsForGivenId(event.id.toString()) }
    }
}