package com.inigo.arch.user.domain

interface UserStore {
    fun checkByUsernameAndPassword(username: Username, password: Password): AuthenticationData
    fun save(user: User)
    fun delete(user: User)
    fun existsUserId(user: User): Boolean
    fun existsUsername(user: User): Boolean
    fun existsEmail(user: User): Boolean
}