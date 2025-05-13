package com.gym.fitmentor.users.coach.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Spring Data JPA repository for the Coach entity.
 */
@Repository
interface CoachRepository : JpaRepository<CoachJPA, Long> {
}
