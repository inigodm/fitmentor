package com.inigo.fitmentor.client.domain

import java.util.UUID

/**
 * Service Interface for managing [com.inigo.fitmentor.client.infrastructure.ClientJpa].
 */
interface ClientService {
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
    fun findOne(id: UUID): Client?

    /**
     * Delete the "id" client.
     *
     * @param id the id of the entity.
     */
    fun delete(id: UUID)
}
