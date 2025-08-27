package com.inigo.fitmentor.plan.coach.application

import com.inigo.fitmentor.plan.coach.model.Plan
import com.inigo.fitmentor.plan.coach.model.PlanStore
import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.CoachId
import org.springframework.stereotype.Component

@Component
class GetPlans(val planStore: PlanStore) {
    fun execute(clientId: ClientId, coachId: CoachId): List<Plan> {
        return planStore.findByClientAndCoach(clientId, coachId)
    }
}