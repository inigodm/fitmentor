package com.inigo.fitmentor.plan.nutrition.foods.infrastructure

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table
    (name = "foods")
class FoodJpa(
    @Id
    @Column(name = "id", nullable = false)
    var id: UUID = UUID.randomUUID(),
    @Column(name = "name")
    var name: String,
    @Column(name = "cal_per_100g")
    var calPer100g: Int,
    @Column(name = "protein_per_100g")
    var proteinPer100g: Int,
    @Column(name = "carbohydrate_per_100g")
    var carbohydratePer100g: Int,
    @Column(name = "fat_per_100g")
    var fatPer100g: Int
) {
    fun toDomain() = com.inigo.fitmentor.plan.nutrition.foods.domain.Food(
        id = this.id,
        name = this.name,
        calPer100g = this.calPer100g / 10.0,
        proteinPer100g = this.proteinPer100g / 10.0,
        carbohydratePer100g = this.carbohydratePer100g / 10.0,
        fatPer100g = this.fatPer100g / 10.0
    )
}