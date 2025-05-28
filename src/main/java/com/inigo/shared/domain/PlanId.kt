package com.inigo.shared.domain

import java.util.UUID

data class PlanId(val value: UUID) {
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(value: String): PlanId = PlanId(UUID.fromString(value))
        fun random(): PlanId = PlanId(UUID.randomUUID())
    }
}