package com.inigo.arch.user.domain

interface TokenGenerator {
    fun generateTokenFor(username: Username, password: Password): Token
}