package com.inigo.shared.domain

import com.inigo.shared.domain.ClientId
import java.util.UUID

data class PlanId(val value: UUID) {
    constructor(uuid: String) : this(UUID.fromString(uuid))
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(value: String): PlanId = PlanId(UUID.fromString(value))
        fun random(): PlanId = PlanId(UUID.randomUUID())
    }
}