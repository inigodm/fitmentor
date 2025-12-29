package com.inigo.fitmentor.plan.nutrition.plan.domain

interface NutritionStore {
    fun update(nutritionPlan: NutritionPlan)
    fun save(nutritionPlan: NutritionPlan)
}