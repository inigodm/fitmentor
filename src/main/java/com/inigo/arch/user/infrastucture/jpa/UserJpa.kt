package com.inigo.arch.user.infrastucture.jpa

import com.inigo.arch.user.domain.Email
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Role
import com.inigo.arch.user.domain.User
import com.inigo.arch.user.domain.Username
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
            username = Username(username),
            email = Email(email),
            password = Password(password),
            role = Role.valueOf(role)
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