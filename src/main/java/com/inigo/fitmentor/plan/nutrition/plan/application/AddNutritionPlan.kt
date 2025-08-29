package com.inigo.fitmentor.plan.nutrition.plan.application

import com.inigo.fitmentor.plan.nutrition.plan.domain.NutritionPlan
import com.inigo.fitmentor.plan.nutrition.plan.domain.NutritionStore
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class AddNutritionPlan(
    private val repository: NutritionStore
) {
    fun execute(request: AddNutritionPlanRequest): NutritionPlan {
        val plan = NutritionPlan(
            clientId = request.clientId,
            coachId = request.coachId,
            description = request.description,
            startDate = request.startDate,
            endDate = request.endDate
        )
        repository.save(plan)
        return plan
    }
}

data class AddNutritionPlanRequest(
    var clientId: UUID,
    var coachId: UUID,
    val description: String,
    val startDate: Instant,
    val endDate: Instant
)
