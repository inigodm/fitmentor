package com.inigo.shared.domain

import com.inigo.shared.domain.ClientId
import java.util.UUID

data class TimeSlotId(val value: UUID) {
    constructor(uuid: String) : this(UUID.fromString(uuid))
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(value: String): TimeSlotId = TimeSlotId(UUID.fromString(value))
        fun random(): TimeSlotId = TimeSlotId(UUID.randomUUID())
    }
}