package com.inigo.arch.spring

import com.inigo.arch.user.domain.TokenService
import com.inigo.arch.user.infrastucture.spring.LoggedInUser
import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtAuthenticationFilter(
    val tokenService: TokenService,
    @Value("\${jwt.secret}") val secretKey: String
) : OncePerRequestFilter() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (headerIsInvalid(header)) {
            filterChain.doFilter(request, response)
            return
        }
        val tok: String? = header.substring(7)
        try {
            // Validar el token usando el servicio de tokens
            val user = tokenService.parseToken(tok!!)
            println(user.id)
            println("Is a valid signature? ${tokenService.isSignatureValid(tok)}")
            println("Token: $tok")
            println("Secret: ${secretKey}")
        } catch (ex: Exception) {
            // Manejar el caso de token inválido
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido: " + ex.message)
            return
        }
        println("Token is valid")
        val token = createToken(header)
        println("Configurando SecurityContext con el token: $token")
        SecurityContextHolder.getContext().authentication = token
        println("SecurityContext configurado: ${SecurityContextHolder.getContext().authentication}")
        filterChain.doFilter(request, response)
    }

    private fun headerIsInvalid(authorizationHeader: String?): Boolean {
        return authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")
    }

    private fun createToken(authorizationHeader: String): UsernamePasswordAuthenticationToken {
        val userPrincipal = tokenService.parseToken(authorizationHeader)
        println("UserPrincipal: $userPrincipal")
        return UsernamePasswordAuthenticationToken(userPrincipal, userPrincipal, mutableListOf<GrantedAuthority?>())
    }
}