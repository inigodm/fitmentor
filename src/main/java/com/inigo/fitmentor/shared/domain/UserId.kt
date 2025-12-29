package com.inigo.fitmentor.shared.domain

import java.util.UUID

data class UserId(val value: UUID) {
    constructor(uuid: String) : this(UUID.fromString(uuid))
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(value: String): UserId = UserId(UUID.fromString(value))
        fun random(): UserId = UserId(UUID.randomUUID())
    }
}