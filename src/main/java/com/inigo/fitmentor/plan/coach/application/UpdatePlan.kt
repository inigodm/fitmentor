package com.inigo.fitmentor.plan.coach.application

import com.inigo.fitmentor.plan.coach.model.Plan
import com.inigo.fitmentor.plan.coach.model.PlanStore
import org.springframework.stereotype.Component

@Component
class UpdatePlan(val store: PlanStore) {

    fun execute(plan: Plan) {
        plan.save(store)
    }
}