package com.gym.fitmentor.users.client.infrastructure

import com.gym.fitmentor.users.client.application.CreateClient
import com.gym.fitmentor.users.client.application.FindClient
import com.gym.fitmentor.users.client.application.UpdateClient
import com.inigo.fitmentor.client.domain.Client
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * REST controller for managing [ClientJpa].
 */
@RestController
@RequestMapping("/clients")
@Validated
class ClientController(val createClient: CreateClient, val findClient: FindClient, val updateClient: UpdateClient) {

    /**
     * `GET  /clients/:id` : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the clientDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/{id}")
    fun getClient(@NotNull @PathVariable("id") id: UUID): ResponseEntity<Client> {
        LOG.debug("REST request to get Client : {}", id)
        val client: Client? = findClient.execute(id)
        return ResponseEntity.ok(client)
    }

    @PutMapping()
    fun updateClient(@Valid @RequestBody client: Client): ResponseEntity<*> {
        LOG.debug("REST request to update Client : {}", client.id)

        val updatedClient = updateClient.execute(client)
        return ResponseEntity.ok("")
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ClientController::class.java)
    }
}
