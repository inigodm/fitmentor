package com.inigo.fitmentor.plan.nutrition.plan.domain

import java.util.*
import java.time.Instant

@JvmInline
value class NutritionPlanId(val value: UUID)

class NutritionPlan(
    val id: NutritionPlanId = NutritionPlanId(UUID.randomUUID()),
    val clientId: UUID,
    val coachId: UUID,
    val description: String,
    val startDate: Instant,
    val endDate: Instant
) {
    fun update(repository: NutritionStore) {
        repository.update(this)
    }
}
