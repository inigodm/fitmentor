package com.inigo.fitmentor.plan.nutrition.plan.infrastructure

import com.inigo.fitmentor.plan.nutrition.plan.application.AddNutritionPlan
import com.inigo.fitmentor.plan.nutrition.plan.application.AddNutritionPlanRequest
import com.inigo.fitmentor.plan.nutrition.meat.application.AddMealToNutritionPlan
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/nutrition/plans")
class NutritionPlanController(
    private val addNutritionPlan: AddNutritionPlan,
    private val addMealToNutritionPlan: AddMealToNutritionPlan
) {
    @PostMapping
    fun addPlan(@RequestBody request: AddNutritionPlanRequest): ResponseEntity<Void> {
        addNutritionPlan.execute(request)
        return ResponseEntity.ok().build()
    }
}
