package com.inigo.fitmentor.coach.domain

import com.inigo.fitmentor.shared.domain.CoachId

/**
 * Service Interface for managing [com.inigo.fitmentor.client.infrastructure.ClientJpa].
 */
interface CoachService {
    fun save(coach: Coach): Coach

    fun findByCoachId(id: CoachId): Coach?
    fun findAll(): List<Coach>
    fun existsUser(coach: Coach): Boolean
    fun existsCoach(root: Coach): Boolean

}
