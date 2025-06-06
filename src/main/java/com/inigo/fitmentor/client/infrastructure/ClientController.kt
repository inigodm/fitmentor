package com.inigo.fitmentor.client.infrastructure

import com.inigo.fitmentor.client.application.CreateClient
import com.inigo.fitmentor.client.application.FindClient
import com.inigo.fitmentor.client.application.UpdateClient
import com.inigo.fitmentor.client.domain.Client
import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.UserId
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import java.util.*

/**
 * REST controller for managing [ClientJpa].
 */
@RestController
@RequestMapping("/user/clients")
@Validated
class ClientController(
    val findClient: FindClient,
    val updateClient: UpdateClient,
    val createClient: CreateClient) {

    /**
     * `GET  /clients/:id` : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the clientDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/{id}")
    fun getClient(@NotNull @PathVariable("id") id: UUID): ResponseEntity<ClientResponse> {
        LOG.debug("REST request to get Client : {}", id)
        val client: Client? = findClient.execute(ClientId(id))
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
    fun updateClient(@Valid @RequestBody client: ClientCreationRequest): ResponseEntity<*> {
        LOG.debug("REST request to update Client : {}", client.id)

        val updatedClient = updateClient.execute(toDomain(client))
        return ResponseEntity.ok("")
    }

    @PostMapping()
    fun createClient(@Valid @RequestBody client: ClientCreationRequest): ResponseEntity<*> {
        LOG.debug("REST request to create Client : {}", client.id)

        val updatedClient = createClient.execute(toDomain(client))
        return ResponseEntity.ok("")
    }

    fun toDomain(clientReq: ClientCreationRequest): Client {
        return with(clientReq) {
            Client(
                id = id,
                goals = goals,
                age = age,
                injuries = injuries,
                weight = weight,
                equipmentAccess = equipmentAccess,
                phonenumber = phonenumber,
                user = user
            )
        }
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ClientController::class.java)
    }

    data class ClientCreationRequest(
        @field:NotNull(message = "id must not be null") var id: ClientId,
        var goals: String? = null,
        var age: Int? = null,
        var injuries: String? = null,
        var weight: Int? = null,
        var equipmentAccess: Int? = null,
        var phonenumber: String? = null,
        @field:NotNull(message = "user must not be null") var user: UserId,
        var plans: List<UUID>? = null
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
