package com.inigo.arch.user.domain

import java.util.Base64
import java.util.UUID

data class User (val id: UUID,
            val username: Username,
            val email: Email,
            val password: Password,
            val role: Role,
            var fido2: Fido2? = null) {

    fun isChallengeExpired(): Boolean {
        return fido2?.challengeExpiry?.isBefore(java.time.Instant.now()) ?: true
    }

    fun updateFido(store: UserStore,
                   publicKey: ByteArray,
                   credentialId: ByteArray) {
        fido2 = Fido2(
            credentialId = Base64.getEncoder().withoutPadding().encodeToString(credentialId),
            publicKey = Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey),
            counter = 0,
            currentChallenge = null,
            challengeExpiry = null
        )
        store.save(this)
    }
}