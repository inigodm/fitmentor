package com.inigo.arch.user.infrastucture

import com.inigo.arch.user.domain.FidoService
import com.inigo.arch.user.domain.User
import com.webauthn4j.WebAuthnManager
import com.webauthn4j.converter.AttestedCredentialDataConverter
import com.webauthn4j.converter.CollectedClientDataConverter
import com.webauthn4j.converter.util.ObjectConverter
import com.webauthn4j.data.PublicKeyCredentialParameters
import com.webauthn4j.data.PublicKeyCredentialType
import com.webauthn4j.data.RegistrationParameters
import com.webauthn4j.data.RegistrationRequest
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.Base64

@Component
class FidoWebAuthn4jService(val objectConverter: ObjectConverter, val attestedCredentialDataConverter: AttestedCredentialDataConverter): FidoService {
    @Value("\${fitmentor.domain}") lateinit var DOMAIN: String

    /**
     * returns:
     *  A pair with:
     *   1- the public key, as ByteArray
     *   2- the credentialId as byteArray
     */
    override fun registerData(
        attestationObject: ByteArray,
        clientDataJSONBytes: ByteArray,
        challengeFromClient: ByteArray
    ): Pair<ByteArray, ByteArray> {
        val registrationRequest = RegistrationRequest(attestationObject, clientDataJSONBytes)
        val registrationParameters = RegistrationParameters(
            ServerProperty(
                Origin("https://" + DOMAIN),
                DOMAIN,
                DefaultChallenge(challengeFromClient)
            ),
            listOf(
                PublicKeyCredentialParameters(
                    PublicKeyCredentialType.PUBLIC_KEY,
                    COSEAlgorithmIdentifier.ES256
                ),
                PublicKeyCredentialParameters(
                    PublicKeyCredentialType.PUBLIC_KEY,
                    COSEAlgorithmIdentifier.RS256
                )
            ),
            true
        )

        val webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager()

        val registrationData =
            webAuthnManager.parse(registrationRequest)
        webAuthnManager.verify(registrationData, registrationParameters)
        val attestedCredentialData = registrationData.attestationObject!!.authenticatorData.attestedCredentialData!!
        return Pair(obtainPublicKey(attestedCredentialData), attestedCredentialData.credentialId)
    }

    override fun assertValidChallenge(
        clientDataJSONBytes: ByteArray,
        user: User
    ): ByteArray {
        val challengeFromClient = CollectedClientDataConverter(objectConverter)
            .convert(clientDataJSONBytes)!!.challenge.value

        val expectedChallenge: String? = user.fido2?.currentChallenge

        if (!MessageDigest.isEqual(
                Base64.getUrlDecoder().decode(expectedChallenge),
                challengeFromClient
            )
        ) {
            throw RuntimeException("Challenge inválido")
        }
        return challengeFromClient
    }

    override fun obtainPublicKey(
        record: AttestedCredentialData
    ): ByteArray {
        return AttestedCredentialDataConverter(objectConverter).convert(record)
    }
}