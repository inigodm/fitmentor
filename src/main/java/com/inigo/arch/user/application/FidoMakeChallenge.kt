package com.inigo.arch.user.application

import com.inigo.arch.user.domain.UserStore
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.Base64
import java.util.UUID

@Component
class FidoMakeChallenge(val userStore: UserStore) {
    fun execute(userId: String): String {
        if (!userStore.existsUserId(UUID.fromString(userId))) {
            throw IllegalArgumentException("User with ID $userId does not exist")
        }
        val challenge = ByteArray(32)
        SecureRandom().nextBytes(challenge)
        val challengeStr = Base64.getUrlEncoder().withoutPadding().encodeToString(challenge)
        userStore.updateChallenge(userId, challengeStr)
        return challengeStr
    }
}