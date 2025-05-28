package com.inigo.shared.domain

import java.util.UUID

data class TimeSlotId(val value: UUID) {
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(value: String): TimeSlotId = TimeSlotId(UUID.fromString(value))
        fun random(): TimeSlotId = TimeSlotId(UUID.randomUUID())
    }
}