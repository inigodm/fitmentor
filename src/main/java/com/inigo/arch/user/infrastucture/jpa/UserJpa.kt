package com.inigo.arch.user.infrastucture.jpa

import com.inigo.arch.user.domain.Email
import com.inigo.arch.user.domain.Fido2
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Role
import com.inigo.arch.user.domain.User
import com.inigo.arch.user.domain.Username
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(name = "users")
class UserJpa(
    @Id
    @Column(name = "id", nullable = false)
    var id: UUID,
    @Column(name = "username", nullable = false)
    var username: String,
    @Column(name = "email", nullable = false)
    var email: String,
    @Column(name = "password", nullable = false)
    var password: String,
    @Column(name = "role", nullable = false)
    var role: String,
    @Column(name = "fido2_credential_id")
    var fido2credentialId: String? = null,
    @Column(name = "fido2_public_key")
    var fido2publicKey: String? = null,
    @Column(name = "fido2_counter")
    var fido2counter: Int = 0,
    @Column(name = "current_challenge")
    var currentChallenge: String? = null,
    @Column(name = "challenge_expiry")
    var challengeExpiry: Instant? = null) {

    fun toDomain(): User {
        val fido2 = fido2credentialId?.let { credId ->
            fido2publicKey?.let { pubKey ->
                challengeExpiry?.let { challengeExpiry ->
                    currentChallenge?.let { currentChallenge ->
                        Fido2(credId, pubKey, fido2counter, currentChallenge, challengeExpiry)
                    }
                }
            }
        }
        return User(
            id = id,
            username = Username(username),
            email = Email(email),
            password = Password(password),
            role = Role.valueOf(role),
            fido2 = fido2
        )
    }

    constructor() : this(
        id = UUID.randomUUID(),
        username = "",
        email = "",
        password = "",
        role = "",
        fido2credentialId = null,
        fido2publicKey = null,
        fido2counter = 0
    )
}