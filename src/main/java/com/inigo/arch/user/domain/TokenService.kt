package com.inigo.arch.user.domain

import com.inigo.arch.user.infrastucture.spring.LoggedInUser
import java.util.*

interface TokenService {
    fun generateToken(username: String, email: String, id: UUID, userRole: Int): String

    fun parseToken(bearer: String): LoggedInUser

    fun isSignatureValid(token: String): Boolean
}
