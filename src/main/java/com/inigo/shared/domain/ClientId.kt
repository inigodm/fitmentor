package com.inigo.shared.domain

import java.util.UUID

data class ClientId(val value: UUID) {
    override fun toString(): String = value.toString()
    constructor(uuid: String) : this(UUID.fromString(uuid))
    companion object {
        fun fromString(value: String): ClientId = ClientId(UUID.fromString(value))
        fun random(): ClientId = ClientId(UUID.randomUUID())
    }
}