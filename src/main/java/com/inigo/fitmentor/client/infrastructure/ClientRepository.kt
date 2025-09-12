package com.inigo.fitmentor.client.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/**
 * Spring Data JPA repository for the Client entity.
 */
@Repository
interface ClientRepository : JpaRepository<ClientJpa, UUID> {
    @Query("SELECT c FROM ClientJpa c WHERE c.user = :userId")
    fun findByUserId(userId: UUID): Optional<ClientJpa>
}
