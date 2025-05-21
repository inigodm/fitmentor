package com.inigo.arch.user.domain

interface UserStore {
    fun checkByUsernameAndPassword(username: Username, password: Password): User
    fun save(user: User)
    fun delete(user: User)
}