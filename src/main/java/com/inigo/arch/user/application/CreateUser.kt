package com.inigo.arch.user.application

import com.inigo.arch.user.domain.Email
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Role
import com.inigo.arch.user.domain.User
import com.inigo.arch.user.domain.UserStore
import com.inigo.arch.user.domain.Username
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CreateUser(val store: UserStore) {
    fun execute(
        id: UUID,
        username: Username,
        email: Email,
        password: Password,
        role: Role
    ) {
        val user = User(
            id = id,
            username = username,
            email = email,
            password = password,
            role = role
        )
        if (store.existsUserId(user)) {
            throw IllegalArgumentException("User with id ${id} already exists")
        }
        if (store.existsEmail(user)) {
            throw IllegalArgumentException("User with id ${email} already exists")
        }
        if (store.existsUsername(user)) {
            throw IllegalArgumentException("User with username ${username} already exists")
        }
        store.save(user)
    }
}