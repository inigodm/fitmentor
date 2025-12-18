package com.inigo.arch.user.infrastucture.jpa

import com.inigo.arch.user.domain.AuthenticationData
import com.inigo.arch.user.domain.User
import com.inigo.arch.user.domain.UserStore
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Role
import com.inigo.arch.user.domain.Username
import com.inigo.arch.user.infrastucture.UnauthorizedError
import com.inigo.arch.shared.domain.errors.NotFoundError
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

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
        return when (Role.valueOf(user.role.uppercase())) {
            Role.COACH -> AuthenticationData(
                user.id,
                null,
                repo.findCoachIdByUserId(user.id) ?: throw NotFoundError.becauseUserIsNotCoach(user.id.toString()),
                user.username,
                user.email,
                Role.COACH.ordinal
            )
            Role.CLIENT -> AuthenticationData(
                user.id,
                repo.findClientIdByUserId(user.id) ?: throw NotFoundError.becauseUserIsNotClient(user.id.toString()),
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

    override fun save(user: User): User {
        return repo.save(
            UserJpa(user.userId,
                user.username.value,
                user.email.value,
                bCryptEncoder.encode(user.password!!.value),
                user.role.name)
            ).toDomain()
    }

    override fun delete(user: User) {
        repo.delete(
            UserJpa(user.userId,
                user.username.value,
                user.email.value,
                user.password!!.value,
                user.role.name)
        )
    }

    override fun existsUserId(id: UUID) = repo.existsById(id)
    override fun findById(userId: String): User {
        return repo.findById(UUID.fromString(userId))
            .map { it.toDomain() }
            .orElseThrow { NotFoundError.becauseUserIdNotFound(userId) }
    }
    override fun searchById(userId: UUID): User? {
        return repo.findById(userId)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun existsUsername(user: User) = repo.findByUsername(user.username.value).isPresent

    override fun existsEmail(user: User) = repo.findByEmail(user.email.value).isPresent
    override fun updateUserType(userId: UUID, type: String) {
        val updatedRows = repo.updateUserTypeById(userId, type)
        if (updatedRows == 0) {
            throw IllegalArgumentException("User with ID $userId not found or type update failed.")
        }
    }

    override fun updateChallenge(userId: String, challengeStr: String) {
        repo.updateChallenge(UUID.fromString(userId), challengeStr);
    }
}