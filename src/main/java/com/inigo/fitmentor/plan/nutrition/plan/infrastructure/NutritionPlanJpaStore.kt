package com.inigo.fitmentor.plan.nutrition.plan.infrastructure

import com.inigo.fitmentor.plan.nutrition.plan.domain.NutritionPlan
import com.inigo.fitmentor.plan.nutrition.plan.domain.NutritionStore
import org.springframework.stereotype.Component

@Component
class NutritionPlanJpaStore(val repository: NutritionPlanJpaRepository): NutritionStore {

    override fun update(nutritionPlan: NutritionPlan) {
        repository.save(NutritionPlanJpa.from(nutritionPlan))
    }

    override fun save(nutritionPlan: NutritionPlan) {
        repository.save(NutritionPlanJpa.from(nutritionPlan))
    }
}