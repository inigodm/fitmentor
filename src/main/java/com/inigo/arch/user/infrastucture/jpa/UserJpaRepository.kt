package com.inigo.arch.user.infrastucture.jpa

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.Optional
import java.util.UUID

@Repository
interface UserJpaRepository : JpaRepository<UserJpa, UUID> {
    fun findByUsername(username: String): Optional<UserJpa>

    @Query("SELECT id FROM ClientJpa c WHERE c.user = :userId")
    fun findClientIdByUserId(@Param("userId") userId: UUID): UUID?

    @Query("SELECT id FROM CoachJpa c WHERE c.user = :userId")
    fun findCoachIdByUserId(@Param("userId") userId: UUID): UUID?
    fun findByEmail(email: String): Optional<UserJpa>
    @Modifying
    @Transactional
    @Query("UPDATE UserJpa u SET u.role = :role WHERE u.id = :id")
    fun updateUserTypeById(id: UUID, role: String): Int

    @Modifying
    @Transactional
    @Query("UPDATE UserJpa u SET u.currentChallenge = :challenge, u.challengeExpiry = :expiry WHERE u.id = :id")
    fun updateChallenge(id: UUID, challenge: String, expiry : Instant = Instant.now().plusSeconds(120))
}