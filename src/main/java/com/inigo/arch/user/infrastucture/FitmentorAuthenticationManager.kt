package com.inigo.arch.user.infrastucture

import com.inigo.arch.user.domain.AuthenticationData
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.UserStore
import com.inigo.arch.user.domain.Username
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import java.util.UUID


@Service
class FitmentorAuthenticationManager(val store: UserStore): AuthenticationManager {
    override fun authenticate(authentication: Authentication): Authentication {
        val authenticationData = store.checkByUsernameAndPassword(
            Username(authentication.principal.toString()),
            Password(authentication.credentials.toString()))
        return UserAuthentication(authenticationData)
    }
}

class UserAuthentication(val authenticationData: AuthenticationData) : Authentication {
    fun getId() : UUID {
        return authenticationData.userId
    }

    override fun getName(): String {
        return authenticationData.username
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
        return authenticationData.username
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }

    fun getEmail(): String = authenticationData.email
    fun getUserRole(): Int = authenticationData.role
    fun getClientId(): UUID? = authenticationData.clientId
    fun getCoachId(): UUID? = authenticationData.coachId

}