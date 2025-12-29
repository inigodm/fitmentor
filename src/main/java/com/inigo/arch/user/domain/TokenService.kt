package com.inigo.arch.user.domain

import com.inigo.arch.spring.LoggedInUser
import java.util.*

interface TokenService {
    fun generateToken(username: String,
                      email: String,
                      id: UUID,
                      clientId: UUID?,
                      coachId: UUID?,
                      userRole: Int): String

    fun parseToken(bearer: String): LoggedInUser

    fun isSignatureValid(token: String): Boolean
}
