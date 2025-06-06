package com.inigo.shared.domain.events

import com.inigo.arch.shared.domain.DomainEvent
import java.util.UUID

data class ClientUpdated(val clientId: UUID, val userId: UUID) : DomainEvent()  {
    override fun name(): String {
        return "ClientUpdated"
    }
}