package com.inigo.arch.user.domain

@JvmInline
value class Password(val value: String) {
    init {
        require(value.isNotBlank()) { "Password cannot be void" }
    }
}