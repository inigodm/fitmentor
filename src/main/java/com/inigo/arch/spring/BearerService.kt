package com.inigo.arch.spring

import com.inigo.arch.user.domain.TokenService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class BearerService(
    @Value("\${jwt.secret}") val JWT_SECRET: String
)  : TokenService{
    override fun generateToken(
        username: String,
        email: String,
        id: UUID,
        clientId: UUID?,
        coachId: UUID?,
        userRole: Int): String {
        val key: Key = Keys.hmacShaKeyFor(JWT_SECRET.toByteArray())

        val compactTokenString = Jwts.builder()
            .claim("jti", UUID.randomUUID().toString())
            .claim("id", id)
            .claim("sub", username)
            .claim("name", username)
            .claim("email", email)
            .claim("userRole", userRole)
            .claim("clientId", clientId?.toString())
            .claim("coachId", coachId?.toString())
            .setExpiration(null)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return "Bearer $compactTokenString"
    }

    override fun parseToken(bearer: String): LoggedInUser {
        val token = bearer.replace("Bearer ", "")
        val secretBytes = JWT_SECRET.toByteArray()

        val jwsClaims = Jwts.parserBuilder()
            .setSigningKey(secretBytes)
            .build()
            .parseClaimsJws(token) //Jwt, plaintext....
        val username = jwsClaims.getBody().subject
        val userId = UUID.fromString(jwsClaims.getBody()!!.get("id", String::class.java))
        val email = jwsClaims.getBody().get("email", String::class.java)
        val userRole = jwsClaims.getBody().get("userRole", Integer::class.java).toInt()
        val clientId = jwsClaims.getBody().get("clientId", String::class.java)
        val coachId = jwsClaims.getBody().get("coachId", String::class.java)
        return LoggedInUser(username, email, userId, userRole, clientId, coachId)
    }

    override fun isSignatureValid(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET.toByteArray())
                .build()
                .parseClaimsJws(token)
            true
        } catch (ex: Exception) {
            println("Error al validar el token: ${ex.message}")
            false
        }
    }
}

@JvmRecord
data class LoggedInUser(val name: String, val email: String, val id: UUID, val userRole: Int, val clientId: String?, val coachId: String?) {
    fun isCoach(): Boolean = userRole == 1
    fun isClient(): Boolean = userRole == 2
    fun isAdmin(): Boolean = userRole == 3
}