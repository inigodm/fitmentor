package com.inigo.fitmentor.shared.domain.events

import com.inigo.arch.shared.domain.DomainEvent
import java.util.UUID

open class ClientUpdated(
    val name: String = "client.updated",
    val clientId: UUID,
    val userId: UUID): DomainEvent() {
    override fun name(): String {
        return name
    }
}