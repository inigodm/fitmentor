package com.inigo.arch.user.infrastucture

import com.inigo.arch.user.application.Login
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Username
import lombok.AllArgsConstructor
import lombok.Data
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(val login: Login) {
    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<*> {
        val token = login.execute(Username(request.username!!), Password(request.password!!))
        return ResponseEntity.ok(AuthResponse(token.value))
    }
}

@Data
class AuthRequest {
    val username: String? = null
    val password: String? = null
}
@Data
@AllArgsConstructor
data class AuthResponse (val token: String)