package com.inigo.arch.user.domain

@JvmInline
value class Email(val value: String) {
    init {
        require(value.isNotBlank()) { "Email cannot be void" }
    }
}