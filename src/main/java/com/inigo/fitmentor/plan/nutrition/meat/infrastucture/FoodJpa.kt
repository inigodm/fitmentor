package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

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
    var calPer100g: Double,
    @Column(name = "protein_per_100g")
    var proteinPer100g: Double,
    @Column(name = "carbohydrate_per_100g")
    var carbohydratePer100g: Double,
    @Column(name = "fat_per_100g")
    var fatPer100g: Double
)

