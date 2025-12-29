package com.inigo.arch.user.domain

import com.webauthn4j.data.RegistrationData
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData

interface FidoService {
    fun registerData(
        attestationObject: ByteArray,
        clientDataJSONBytes: ByteArray,
        challengeFromClient: ByteArray
    ): Pair<ByteArray, ByteArray>

    fun assertValidChallenge(clientDataJSONBytes: ByteArray, user: User): ByteArray
    fun obtainPublicKey(record: AttestedCredentialData): ByteArray
}