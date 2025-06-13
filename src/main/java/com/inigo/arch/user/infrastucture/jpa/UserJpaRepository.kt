package com.inigo.arch.user.infrastucture.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.NativeQuery
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserJpaRepository : JpaRepository<UserJpa, UUID> {
    fun findByUsername(username: String): Optional<UserJpa>

    @Query("SELECT id FROM clients c WHERE c.user_id = :userId", nativeQuery = true)
    fun findClientIdByuserId(@Param("userId") userId: String): UUID

    @Query("SELECT id FROM coachs c WHERE c.user_id = :userId", nativeQuery = true)
    fun findCoachIdByuserId(@Param("userId") userId: String): UUID
    fun findByEmail(email: String): Optional<UserJpa>
}