package com.inigo.fitmentor.shared.infrastructure

import com.inigo.arch.user.application.CreateUser
import com.inigo.arch.user.domain.Email
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Username
import com.inigo.fitmentor.shared.domain.FitmentorUser
import com.inigo.fitmentor.shared.domain.Users
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(val createUser: CreateUser): Users {


    override fun createUser(user: FitmentorUser) {
        createUser.execute(
            id = user.user?.value ?: UUID.randomUUID(),
            username = Username(user.username),
            email = Email(user.email),
            role = user.role,
            password = Password.Companion.VOID
        )
    }
}