package com.inigo.arch.domainevents

import com.inigo.arch.shared.domain.DomainEvent
import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.UserId

data class ClientCreated(val cliewntId: ClientId, val userId: UserId) : DomainEvent()  {
    override fun name(): String {
        return "ClientCreated"
    }
}