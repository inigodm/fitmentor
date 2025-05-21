package com.inigo.arch.user.domain

@JvmInline
value class Token (val value: String) {
    init {
        require(value.isNotBlank()) { "Token cannot be void" }
    }
}