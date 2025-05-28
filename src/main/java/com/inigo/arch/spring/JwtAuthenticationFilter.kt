package com.inigo.arch.spring

import com.inigo.arch.user.domain.TokenService
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
    val tokenService: TokenService
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
        val token = createToken(header)
        SecurityContextHolder.getContext().authentication = token
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