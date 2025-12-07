package com.inigo.fitmentor.client.domain

import com.inigo.fitmentor.shared.domain.UserId
import java.util.UUID

/**
 * Service Interface for managing [com.inigo.fitmentor.client.infrastructure.ClientJpa].
 */
interface ClientStore {
    /**
     * Save a client.
     *
     * @param client the entity to save.
     * @return the persisted entity.
     */
    fun save(client: Client)

    /**
     * Get all the clients.
     *
     * @return the list of entities.
     */
    fun findAll(): List<Client>

    /**
     * Get the "id" client.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findByUserId(id: UserId): Client?

    /**
     * Delete the "id" client.
     *
     * @param id the id of the entity.
     */
    fun delete(id: UUID)
    fun existsUser(client: Client): Boolean
    fun existsClient(client: Client): Boolean
}
