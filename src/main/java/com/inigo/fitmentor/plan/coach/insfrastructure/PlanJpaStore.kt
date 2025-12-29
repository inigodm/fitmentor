package com.inigo.fitmentor.plan.coach.insfrastructure

import com.inigo.fitmentor.plan.coach.model.Plan
import com.inigo.fitmentor.plan.coach.model.PlanStore
import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.CoachId
import org.springframework.stereotype.Component

@Component
class PlanJpaStore(val repository: PlanRepository) : PlanStore {

    override fun save(plan: Plan) {
        repository.save(PlanJpa.fromDomain(plan))
    }

    override fun findByClientAndCoach(clientId: ClientId, coachId: CoachId) =
        repository.findByClientAndCoach(clientId.value, coachId.value)
            .map { it.toDomain() }
            .toList()
}