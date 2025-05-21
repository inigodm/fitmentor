package com.inigo.arch.user.infrastucture.spring

import com.inigo.arch.user.domain.TokenService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class BearerService : TokenService {
    private val JWT_SECRET = """
    random string dfsdfenrñjweugb
    must be a long string in order to 
    have more than 256 bytes, so I think that I should 
    write some more characters here: 
    lets see: 1, 2, 3

""".trimIndent()

    override fun generateToken(username: String, email: String, id: UUID, userRole: Int): String {
        val key: Key = Keys.hmacShaKeyFor(JWT_SECRET.toByteArray())

        val compactTokenString = Jwts.builder()
            .claim("jti", UUID.randomUUID().toString())
            .claim("id", id)
            .claim("sub", username)
            .claim("email", email)
            .claim("userRole", userRole)
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
        val userId = UUID.fromString(jwsClaims.getBody()!!.get<String?>("id", String::class.java))
        val email = jwsClaims.getBody().get("email", String::class.java)
        val userRole = jwsClaims.getBody().get("userRole", Integer::class.java).toInt()
        return LoggedInUser(username, email, userId, userRole)
    }
}

@JvmRecord
data class LoggedInUser(val name: String, val email: String, val id: UUID, val userRole: Int)