package com.gym.fitmentor.users.client.infrastructure

import com.gym.fitmentor.users.client.application.CreateClient
import com.gym.fitmentor.users.client.application.FindClient
import com.gym.fitmentor.users.client.application.UpdateClient
import com.gym.fitmentor.users.client.domain.Client
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.jhipster.web.util.ResponseUtil
import java.util.*

/**
 * REST controller for managing [ClientJPA].
 */
@RestController
@RequestMapping("/api/clients")
class ClientController(val createClient: CreateClient, val findClient: FindClient, val updateClient: UpdateClient) {

    /**
     * `GET  /clients/:id` : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the clientDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/{id}")
    fun getClient(@PathVariable("id") id: UUID): ResponseEntity<Client> {
        LOG.debug("REST request to get Client : {}", id)
        val client: Client? = findClient.execute(id)
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(client))
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(ClientController::class.java)
    }
}
