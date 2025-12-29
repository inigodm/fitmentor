package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

import com.inigo.arch.spring.LoggedInUser
import com.inigo.fitmentor.plan.nutrition.meat.application.AddMealToNutritionPlan
import com.inigo.fitmentor.plan.nutrition.meat.application.AddMealToNutritionPlanRequest
import com.inigo.fitmentor.plan.nutrition.meat.application.MealRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/nutrition/plans")
class MealController(
    private val addMealToNutritionPlan: AddMealToNutritionPlan) {
    @PostMapping("/{planId}/meals")
    fun addMeal(
        @PathVariable planId: UUID,
        @RequestBody meal: MealRequest
    ): ResponseEntity<Void> {
        val loggedInUser = SecurityContextHolder.getContext().authentication.principal as LoggedInUser
        val coachId = UUID.fromString(loggedInUser.coachId!!)
        val clientId = UUID.fromString(loggedInUser.clientId!!)
        addMealToNutritionPlan.execute(
            AddMealToNutritionPlanRequest(
                planId = planId,
                meal = meal,
                coachId = coachId,
                clientId = clientId
                )
        )
        return ResponseEntity.ok().build()
    }
}