package com.inigo.shared.domain.events


import java.util.UUID

class ClientCreated(
    name: String = "client.created",
    clientId: UUID,
    userId: UUID
) : ClientUpdated(name, clientId, userId)