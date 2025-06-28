package com.inigo.fitmentor.coach.domain

import com.inigo.arch.shared.domain.AggregateRoot
import com.inigo.fitmentor.client.domain.Client
import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.CoachId
import java.util.UUID

/**
 * Service Interface for managing [com.inigo.fitmentor.client.infrastructure.ClientJpa].
 */
interface CoachService {
    fun save(coach: Coach)

    fun findByCoachId(id: CoachId): Coach?
    fun existsUser(coach: Coach): Boolean
    fun existsCoach(root: Coach): Boolean

}
