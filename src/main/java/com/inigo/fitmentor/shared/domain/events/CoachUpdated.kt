package com.inigo.fitmentor.shared.domain.events

import com.inigo.arch.shared.domain.DomainEvent
import java.util.UUID

open class CoachUpdated(
    val name: String = "coach.updated",
    val coachId: UUID,
    val userId: UUID): DomainEvent() {
    override fun name(): String {
        return name
    }
}