package com.inigo.arch.user.infrastucture.controllers

import com.inigo.arch.user.application.FidoMakeChallenge
import com.inigo.arch.user.application.FidoRegister
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.SecureRandom
import java.util.Base64


@RestController
@RequestMapping("/webauthn")
class WebAuthnController(val fidoMakeChallenge: FidoMakeChallenge, val fidoRegister: FidoRegister) {
    private val random = SecureRandom()

    @GetMapping("/register/challenge")
    fun registerChallenge(@RequestParam userId: String?): String {
        return fidoMakeChallenge.execute(userId!!)
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<*> {
        val clientDataJSONBytes: ByteArray = Base64.getUrlDecoder().decode(request.clientDataJSON)
        val attestationObject: ByteArray = Base64.getUrlDecoder().decode(request.attestationObject)
        fidoRegister.execute(
            request.userId,
            clientDataJSONBytes,
            attestationObject
        )
        return ResponseEntity.ok("Registered")
    }
}

data class RegisterRequest(
    val userId: String,
    val clientDataJSON: String,
    val attestationObject: String
)