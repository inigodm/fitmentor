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
        if (store.existsUserId(user.id)) {
            LOG.warn("User with id ${id} already exists")
            return
        }
        if (store.existsEmail(user)) {
            throw IllegalArgumentException("User with email ${email} already exists")
        }
        if (store.existsUsername(user)) {
            throw IllegalArgumentException("User with username ${username} already exists")
        }
        store.save(user)
    }

    companion object {
        private val LOG = org.slf4j.LoggerFactory.getLogger(CreateUser::class.java)
    }
}