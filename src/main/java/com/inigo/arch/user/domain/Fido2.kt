package com.inigo.arch.user.domain

import java.time.Instant

data class Fido2(
    val credentialId: String,
    val publicKey: String,
    val counter: Int,
    val currentChallenge: String?,
    val challengeExpiry: Instant?)