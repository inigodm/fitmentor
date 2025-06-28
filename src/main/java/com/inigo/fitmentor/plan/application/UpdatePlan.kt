package com.inigo.fitmentor.plan.application

import com.inigo.fitmentor.plan.model.Plan
import com.inigo.fitmentor.plan.model.PlanStore
import org.springframework.stereotype.Component

@Component
class UpdatePlan(val store: PlanStore) {

    fun execute(plan: Plan) {
        plan.save(store)
    }
}