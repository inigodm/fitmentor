package com.inigo.fitmentor.coach.application

import com.inigo.arch.shared.domain.errors.SnapshotNotSendError
import com.inigo.fitmentor.coach.infrastructure.CoachRepository
import com.inigo.fitmentor.shared.domain.events.CoachUpdated
import com.inigo.arch.kafka.KafkaProducerService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ProjectOnCoachUpdated(val repo: CoachRepository, val kafkaProducerService: KafkaProducerService) {
    @EventListener
    fun on(event: CoachUpdated) {
        repo.findById(event.coachId)
            .map { clientJpa ->  clientJpa.toDomain() }
            .map { kafkaProducerService.sendSnapshot(it) }
            .orElseThrow { SnapshotNotSendError.becauseNoClientExistsForGivenId(event.id.toString()) }
    }
}