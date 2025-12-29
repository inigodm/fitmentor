package com.inigo.fitmentor.shared.domain

import java.util.UUID

data class CoachId(val value: UUID) {
    constructor(uuid: String) : this(UUID.fromString(uuid))
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(value: String): CoachId = CoachId(UUID.fromString(value))
        fun random(): CoachId = CoachId(UUID.randomUUID())
    }
}