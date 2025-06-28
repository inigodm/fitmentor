package com.inigo.fitmentor.plan.model

import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.CoachId

interface PlanStore {
    fun save(plan: Plan)
    fun findByClientAndCoach(clientId: ClientId, coachId: CoachId): List<Plan>

}