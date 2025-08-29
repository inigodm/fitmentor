package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

import com.inigo.fitmentor.plan.nutrition.meat.domain.MealComponent
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "meal_components")
class MealComponentJpa(
    @Id
    @Column(name = "id", nullable = false)
    var id: UUID,
    @Column(name = "coach_id")
    var coachId: UUID,
    @Column(name = "client_id")
    var clientId: UUID,
    @Column(name = "meal_id")
    var mealId: UUID,
    @Column(name = "food_id", nullable = false)
    var foodId: UUID,
    @Column(name = "quantity")
    var quantity: Double,
    @Column(name = "unit")
    var unit: String
) {
    constructor() : this(
        id = UUID.randomUUID(),
        coachId = UUID.randomUUID(),
        clientId = UUID.randomUUID(),
        mealId = UUID.randomUUID(),
        foodId = UUID.randomUUID(),
        quantity = 0.0,
        unit = ""
    )

    companion object {
        fun from(component: MealComponent): MealComponentJpa {
            return MealComponentJpa(
                id = component.id,
                coachId = component.coachId,
                clientId = component.clientId,
                mealId = component.mealId,
                foodId = component.foodId,
                quantity = component.quantity,
                unit = component.unit.name
            )
        }
    }
}