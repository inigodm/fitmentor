package com.inigo.arch.user.infrastucture.jpa

import com.inigo.arch.user.domain.AuthenticationData
import com.inigo.arch.user.domain.User
import com.inigo.arch.user.domain.UserStore
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Role
import com.inigo.arch.user.domain.Username
import com.inigo.arch.user.infrastucture.UnauthorizedError
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserRepository(val repo : UserJpaRepository,
                     val bCryptEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()
): UserStore {
    override fun checkByUsernameAndPassword(username: Username, password: Password): AuthenticationData {
        val optUser = repo.findByUsername(username.value)
        if (optUser.isEmpty) {
            throw UnauthorizedError.becauseUserOrPasswordNotFonud(username.value)
        }
        val user = optUser.get()
        if (!bCryptEncoder.matches(password.value, user.password)) {
            throw UnauthorizedError.becauseUserOrPasswordNotFonud(username.value)
        }
        return when (Role.valueOf(user.role)) {
            Role.COACH -> AuthenticationData(
                user.id,
                null,
                repo.findCoachIdByuserId(user.id.toString()),
                user.username,
                user.email,
                Role.COACH.ordinal
            )
            Role.CLIENT -> AuthenticationData(
                user.id,
                repo.findClientIdByuserId(user.id.toString()),
                null,
                user.username,
                user.email,
                Role.CLIENT.ordinal
            )
            //ADMIN and USER roles do not have specific IDs associated with them
            else -> AuthenticationData(
                user.id,
                null,
                null,
                user.username,
                user.email,
                Role.valueOf(user.role).ordinal
            )
        }
    }

    override fun save(user: User) {
        repo.save(
            UserJpa(user.id,
                user.username.value,
                user.email.value,
                bCryptEncoder.encode(user.password.value),
                user.role.name)
            )
        }

    override fun delete(user: User) {
        repo.delete(
            UserJpa(user.id,
                user.username.value,
                user.email.value,
                user.password.value,
                user.role.name)
        )
    }

    override fun existsUserId(user: User) = repo.existsById(user.id)

    override fun existsUsername(user: User) = repo.findByUsername(user.username.value).isPresent

    override fun existsEmail(user: User) = repo.findByEmail(user.email.value).isPresent
}