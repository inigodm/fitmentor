package com.inigo.fitmentor.plan.nutrition.foods.domain

import java.util.UUID

class Food(val id: UUID = UUID.randomUUID(),
           val name: String,
           val calPer100g: Double,
           val proteinPer100g: Double,
           val carbohydratePer100g: Double,
           val fatPer100g: Double) {
    constructor(id: UUID) : this(
        id = id,
        name = "",
        calPer100g = 0.0,
        proteinPer100g = 0.0,
        carbohydratePer100g = 0.0,
        fatPer100g = 0.0
    )
}