package com.inigo.arch.user.infrastucture

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserJpaRepository : JpaRepository<UserJpa, UUID> {
    fun findByUsername(username: String): Optional<UserJpa>
}