package com.inigo.fitmentor.plan.coach.model

import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.CoachId

interface PlanStore {
    fun save(plan: Plan)
    fun findByClientAndCoach(clientId: ClientId, coachId: CoachId): List<Plan>

}