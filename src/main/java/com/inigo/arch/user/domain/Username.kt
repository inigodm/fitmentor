package com.inigo.arch.user.domain

@JvmInline
value class Username(val value: String) {
    init {
        require(value.isNotBlank()) { "Name cannot be void" }
    }
}