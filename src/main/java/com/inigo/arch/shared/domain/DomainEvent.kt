package com.inigo.arch.shared.domain

import com.inigo.arch.shared.infrastructure.UuidUtils
import java.util.UUID

abstract class DomainEvent(val id: UUID = UuidUtils.randomUuidV4()) {
    /**
     * The name of the event, used for logging and debugging purposes.
     * It should be overridden by subclasses to provide a meaningful name.
     */
    abstract fun name(): String

    fun id(): UUID {
        return id
    }
}