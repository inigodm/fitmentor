package com.inigo.shared.domain

import java.util.UUID

data class UserId(val value: UUID) {
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(value: String): UserId = UserId(UUID.fromString(value))
        fun random(): UserId = UserId(UUID.randomUUID())
    }
}