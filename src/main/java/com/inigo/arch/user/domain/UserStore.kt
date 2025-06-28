package com.inigo.arch.user.domain

import java.util.UUID

interface UserStore {
    fun checkByUsernameAndPassword(username: Username, password: Password): AuthenticationData
    fun save(user: User)
    fun delete(user: User)
    fun existsUserId(user: User): Boolean
    fun existsUsername(user: User): Boolean
    fun existsEmail(user: User): Boolean
    fun updateUserType(userId: UUID, type: String)
}