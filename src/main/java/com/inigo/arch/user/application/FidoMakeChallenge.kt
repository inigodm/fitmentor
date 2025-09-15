package com.inigo.arch.user.application

import com.inigo.arch.user.domain.UserStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.Base64
import java.util.UUID

@Component
class FidoMakeChallenge(val userStore: UserStore) {
    @Value("\${fitmentor.domain}") lateinit var DOMAIN: String

    fun execute(userId: String): PublicKeyCredentialCreationOptions {
        val user = userStore.findById(userId)
        val challenge = ByteArray(32)
        SecureRandom().nextBytes(challenge)
        val challengeStr = Base64.getUrlEncoder().withoutPadding().encodeToString(challenge)
        userStore.updateChallenge(userId, challengeStr)
        return PublicKeyCredentialCreationOptions(
            challenge = challengeStr,
            rp = RpEntity(DOMAIN, DOMAIN),
            user = UserEntity(
                id = Base64.getUrlEncoder().withoutPadding().encodeToString(UUID.fromString(userId).toString().toByteArray()),
                name = user.username.value,
                displayName = user.username.value
            ),
            pubKeyCredParams = listOf(
                PubKeyCredParam("public-key", -7),   // ES256
                PubKeyCredParam("public-key", -257)  // RS256
            ),
            authenticatorSelection = AuthenticatorSelection("preferred")
        )
    }
}

data class PublicKeyCredentialCreationOptions(
    val challenge: String,
    val rp: RpEntity,
    val user: UserEntity,
    val pubKeyCredParams: List<PubKeyCredParam>,
    val authenticatorSelection: AuthenticatorSelection? = null,
    val timeout: Long = 60000,
    val attestation: String = "none"
)

data class RpEntity(val name: String, val id: String)
data class UserEntity(val id: String, val name: String, val displayName: String)
data class PubKeyCredParam(val type: String, val alg: Int)
data class AuthenticatorSelection(val userVerification: String)