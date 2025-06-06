package com.inigo.shared.domain.events

import com.inigo.arch.shared.domain.DomainEvent
import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.UserId
import java.util.UUID

data class ClientCreated(val clientId: UUID, val userId: UUID) : DomainEvent()  {
    override fun name(): String {
        return "ClientCreated"
    }
}