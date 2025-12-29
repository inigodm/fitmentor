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
    ): User {
        val user = User(
            userId = id,
            username = username,
            email = email,
            password = password,
            role = role
        )
        store.searchById(user.userId)?.let { return it }

        require(!store.existsEmail(user)) { "User with email ${email} already exists" }
        require(!store.existsUsername(user)) { "User with username ${username} already exists" }

        return store.save(user)
    }
}