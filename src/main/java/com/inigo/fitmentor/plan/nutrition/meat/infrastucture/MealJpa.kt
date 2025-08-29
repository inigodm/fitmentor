package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

import com.inigo.fitmentor.plan.nutrition.meat.domain.Meal
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "meals")
class MealJpa(
    @Id
    @Column(name = "id", nullable = false)
    var id: UUID = UUID.randomUUID(),
    @Column(name = "name")
    var name: String,
    @Column(name = "plan_id")
    var planId: UUID,
) {
    constructor() : this(
        id = UUID.randomUUID(),
        name = "",
        planId = UUID.randomUUID()
    )

    companion object {
        fun from(
            meal: Meal
        ): MealJpa {
            return MealJpa(
                id = meal.id.value,
                name = meal.name,
                planId = meal.planId
            )
        }
    }
}