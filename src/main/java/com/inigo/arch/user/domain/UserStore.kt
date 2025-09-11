package com.inigo.arch.user.domain

import java.util.UUID

interface UserStore {
    fun checkByUsernameAndPassword(username: Username, password: Password): AuthenticationData
    fun save(user: User)
    fun delete(user: User)
    fun existsEmail(user: User): Boolean
    fun updateUserType(userId: UUID, type: String)
    fun updateChallenge(userId: String, challengeStr: String)
    fun existsUsername(user: User): Boolean
    fun existsUserId(id: UUID): Boolean
    fun findById(userId: String): User
}