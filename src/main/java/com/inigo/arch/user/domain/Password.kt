package com.inigo.arch.user.domain

class Password private constructor(val value: String) {
    companion object {
        val VOID = Password("")

        operator fun invoke(value: String?): Password {
            return if (value.isNullOrEmpty()) VOID else Password(value)
        }
    }
}