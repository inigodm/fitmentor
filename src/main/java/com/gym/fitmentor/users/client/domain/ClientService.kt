package com.gym.fitmentor.users.client.domain

import java.util.Optional

/**
 * Service Interface for managing [com.gym.fitmentor.users.client.infrastructure.ClientJPA].
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
    fun findOne(id: Long): Optional<Client>

    /**
     * Delete the "id" client.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
