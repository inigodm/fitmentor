package com.inigo.arch.user.application

import com.inigo.arch.user.domain.Fido2
import com.inigo.arch.user.domain.FidoService
import com.inigo.arch.user.domain.User
import com.inigo.arch.user.domain.UserStore
import com.webauthn4j.WebAuthnManager
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.CollectedClientDataConverter
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.data.AuthenticatorAttestationResponse
import com.webauthn4j.data.PublicKeyCredentialParameters
import com.webauthn4j.data.PublicKeyCredentialType
import com.webauthn4j.data.RegistrationData
import com.webauthn4j.data.RegistrationParameters
import com.webauthn4j.data.RegistrationRequest
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Component
class FidoRegister(val userStore: UserStore, val fidoService: FidoService) {
    fun execute(
        userId: String,
        clientDataJSONBytes: ByteArray,
        attestationObject: ByteArray
    ) {
        val user = obtainUser(userId)
        val challengeFromClient = fidoService.assertValidChallenge(clientDataJSONBytes, user)
        val (publicKey, credentialId) = fidoService.registerData(attestationObject, clientDataJSONBytes, challengeFromClient)
        user.updateFido(userStore, publicKey, credentialId)
    }

    private fun obtainUser(userId: String): User {
        val user: User = userStore.findById(userId)
        if (user.isChallengeExpired()) {
            throw RuntimeException("Challenge expirado")
        }
        return user
    }
}
