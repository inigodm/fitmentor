package com.inigo.arch.user.infrastucture

import com.inigo.arch.user.application.CreateUser
import com.inigo.arch.user.domain.Email
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Role
import com.inigo.arch.user.domain.User
import com.inigo.arch.user.domain.Username
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/user")
class UserController(val createUser: CreateUser) {
    @PutMapping("/create")
    fun save(@RequestBody request: UserCreateRequest): ResponseEntity<String> {
        createUser.execute(
            request.id,
            Username(request.username),
            Email(request.email),
            Password(request.password),
            Role.valueOf(request.role))
        return ResponseEntity.ok("Created")
    }
}

data class UserCreateRequest(
    val id: UUID,
    val username: String,
    val password: String,
    val email: String,
    val role: String
)