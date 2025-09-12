package com.inigo.fitmentor.client.infrastructure

import com.inigo.arch.spring.BearerService
import com.inigo.arch.user.domain.Email
import com.inigo.fitmentor.client.application.CreateClient
import com.inigo.fitmentor.client.application.FindClient
import com.inigo.fitmentor.client.application.UpdateClient
import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.UserId
import io.jsonwebtoken.Jwts
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import java.util.*

/**
 * REST controller for managing [ClientJpa].
 */
@RestController
@RequestMapping("/api/user/clients")
@Validated
class ClientController(
    val findClient: FindClient,
    val updateClient: UpdateClient,
    val createClient: CreateClient,
    val bearerService: BearerService){

    /**
     * `GET  /clients/:id` : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the clientDTO, or with status `404 (Not Found)`.
     */
    @GetMapping
    fun getClient(@NotNull  @RequestHeader("Authorization") authHeader: String): ResponseEntity<ClientResponse> {
        val token = authHeader.removePrefix("Bearer ").trim()
        val data = bearerService.parseToken(token)
        val client: Client? = findClient.execute(UserId(data.id))
        return if (client == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(ClientResponse(
                id = client.id.value,
                goals = client.goals,
                age = client.age,
                injuries = client.injuries,
                weight = client.weight,
                equipmentAccess = client.equipmentAccess,
                phonenumber = client.phonenumber,
                user = client.user.value
            ))
        }
    }

    @PutMapping()
    fun modifyClient(@Valid @RequestBody client: ClientModificationRequest): ResponseEntity<*> {
        LOG.debug("REST request to update Client : {}", client.id)

        updateClient.execute(toDomain(client))
        return ResponseEntity.ok("")
    }

    @PostMapping()
    fun createClient(@Valid @RequestBody client: ClientModificationRequest): ResponseEntity<*> {
        LOG.debug("REST request to create Client : {}", client.id)

        createClient.execute(toDomain(client))
        return ResponseEntity.ok("")
    }

    fun toDomain(clientReq: ClientModificationRequest): Client {
        return with(clientReq) {
            Client(
                id = id,
                goals = goals,
                age = age,
                injuries = injuries,
                weight = weight,
                equipmentAccess = equipmentAccess,
                phonenumber = phonenumber,
                user = user,
                email = email,
                username = username
            )
        }
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ClientController::class.java)
    }

    data class ClientModificationRequest(
        @field:NotNull(message = "id must not be null") var id: ClientId,
        var goals: String? = null,
        var age: Int? = null,
        var injuries: String? = null,
        var weight: Int? = null,
        var equipmentAccess: Int? = null,
        var phonenumber: String? = null,
        @field:NotNull(message = "user must not be null") var user: UserId,
        var plans: List<UUID>? = null,
        var email: String,
        var username: String
    ) : Serializable

    data class ClientResponse(
        val id: UUID,
        val goals: String?,
        val age: Int?,
        val injuries: String?,
        val weight: Int?,
        val equipmentAccess: Int?,
        val phonenumber: String?,
        val user: UUID
    )
}
