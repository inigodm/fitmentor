package com.gym.fitmentor.users.client.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the Client entity.
 */
@Repository
interface ClientRepository : JpaRepository<ClientJPA, Long>
