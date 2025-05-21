package com.inigo.arch.user.infrastucture

import com.inigo.arch.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
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
    var role: String) {
    fun toDomain(): User {
        return User(
            id = id,
            username = com.inigo.arch.user.domain.Username(username),
            email = com.inigo.arch.user.domain.Email(email),
            password = com.inigo.arch.user.domain.Password(password),
            role = com.inigo.arch.user.domain.Role.valueOf(role)
        )
    }

    constructor() : this(
        id = UUID.randomUUID(),
        username = "",
        email = "",
        password = "",
        role = ""
    )
}