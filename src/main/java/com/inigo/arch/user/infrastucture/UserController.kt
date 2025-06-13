package com.inigo.arch.user.infrastucture

import com.inigo.arch.user.application.CreateUser
import com.inigo.arch.user.domain.Email
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Role
import com.inigo.arch.user.domain.User
import com.inigo.arch.user.domain.Username
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/user")
@Validated
class UserController(val createUser: CreateUser) {
    @PutMapping()
    fun save(@Valid @RequestBody request: UserCreateRequest): ResponseEntity<String> {
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
    @field:NotNull(message = "id must not be null")  val id: UUID,
    @field:NotNull(message = "username must not be null") val username: String,
    @field:NotNull(message = "password must not be null") val password: String,
    @field:NotNull(message = "email must not be null") val email: String,
    @field:NotNull(message = "role must not be null") val role: String
)