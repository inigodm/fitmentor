package com.inigo.arch.user.infrastucture

import com.inigo.arch.user.infrastucture.spring.BearerService
import com.inigo.arch.user.infrastucture.spring.UserAuthentication
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Token
import com.inigo.arch.user.domain.TokenGenerator
import com.inigo.arch.user.domain.Username
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class TokenGenerationService(private val authManager: AuthenticationManager, private val bearerService: BearerService):
    TokenGenerator {
    override fun generateTokenFor(username: Username, password: Password): Token {
        val authentication = authManager.authenticate(
            UsernamePasswordAuthenticationToken(username.value, password.value)
        ) as UserAuthentication

        return Token(
            bearerService.generateToken(
                authentication.name,
                authentication.getEmail(),
                authentication.getId(),
                authentication.getUserRole()
            )
        )
    }
}