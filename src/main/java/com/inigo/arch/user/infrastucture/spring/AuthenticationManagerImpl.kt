package com.inigo.arch.user.infrastucture.spring

import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.User
import com.inigo.arch.user.domain.UserStore
import com.inigo.arch.user.domain.Username
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthenticationManagerImpl(val store: UserStore): AuthenticationManager {
    override fun authenticate(authentication: Authentication): Authentication {
        val user = store.checkByUsernameAndPassword(
            Username(authentication.principal.toString()),
            Password(authentication.credentials.toString()))
        return UserAuthentication(user)
    }
}

class UserAuthentication(val user: User) : Authentication {
    fun getId() : UUID {
        return user.id
    }

    override fun getName(): String {
        return user.username.value
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return emptyList()
    }

    override fun getCredentials(): Any {
        return "credentials"
    }

    override fun getDetails(): Any {
        return "details"
    }

    override fun getPrincipal(): Any {
        return user.username.value
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }

    fun getEmail() = user.email.value
    fun getUserRole() = user.role.ordinal
}