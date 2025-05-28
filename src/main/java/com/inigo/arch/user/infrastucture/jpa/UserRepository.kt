package com.inigo.arch.user.infrastucture.jpa

import com.inigo.arch.user.domain.User
import com.inigo.arch.user.domain.UserStore
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Username
import com.inigo.arch.user.infrastucture.UnauthorizedError
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserRepository(val repo : UserJpaRepository,
                     val bCryptEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()
): UserStore {
    override fun checkByUsernameAndPassword(username: Username, password: Password): User {
        val optUser =  repo.findByUsername(username.value)
        if (optUser.isEmpty) {
            throw UnauthorizedError.becauseUserOrPasswordNotFonud(username.value)
        }
        val user = optUser.get()
        if (!bCryptEncoder.matches(password.value, user.password)) {
            throw UnauthorizedError.becauseUserOrPasswordNotFonud(username.value)
        }
        return user.toDomain()
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
}