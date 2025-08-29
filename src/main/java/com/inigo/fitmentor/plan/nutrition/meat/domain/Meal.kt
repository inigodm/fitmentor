package com.inigo.fitmentor.plan.nutrition.meat.domain

import com.inigo.arch.shared.domain.AggregateRoot
import jakarta.transaction.Transactional
import java.util.UUID

@JvmInline
value class MealId(val value: UUID)
@JvmInline
value class FoodId(val value: UUID)
@JvmInline
value class SupplementId(val value: UUID)

class Meal(
    val id: MealId,
    val planId: UUID,
    val name: String,
    val mealComponents: List<MealComponent> = emptyList(),
    val supplementIntakes: List<SupplementIntake> = emptyList()
) : AggregateRoot(aggregateName = "Meal") {

    @Transactional
    fun save(mealStore: MealStore, componentStore: MealComponentStore, supplementIntakeStore: SupplementIntakeStore) {
        mealStore.save(this)
        mealComponents.forEach { componentStore.save(it) }
        supplementIntakes.forEach { supplementIntakeStore.save(it) }
    }
}

class MealComponent(
    val id: UUID,
    val coachId: UUID,
    val clientId: UUID,
    val planId: UUID,
    val mealId: UUID,
    val foodId: UUID,
    val quantity: Double,
    val unit: UnitType
)

enum class UnitType { GR, ML }

class SupplementIntake(
    val id: UUID,
    val coachId: UUID,
    val clientId: UUID,
    val planId: UUID,
    val mealId: UUID,
    val supplementId: SupplementId,
    val quantity: Double,
    val unit: UnitType
)

