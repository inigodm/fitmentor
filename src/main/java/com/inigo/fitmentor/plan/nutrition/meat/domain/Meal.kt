package com.inigo.fitmentor.plan.nutrition.meat.domain

import com.inigo.arch.shared.domain.AggregateRoot
import com.inigo.arch.shared.domain.errors.NotFoundError
import jakarta.transaction.Transactional
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import java.util.UUID

@JvmInline
value class MealId(val value: UUID)
@JvmInline
value class SupplementId(val value: UUID)

class Meal(
    val id: MealId,
    val planId: UUID,
    val name: String,
    val mealComponents: List<MealComponent> = emptyList(),
    val supplementIntakes: List<SupplementIntake> = emptyList()
) : AggregateRoot(aggregateName = "Meal") {

    constructor() : this(
        id = MealId(UUID.randomUUID()),
        planId = UUID.randomUUID(),
        name = "",
        mealComponents = emptyList(),
        supplementIntakes = emptyList()
    )

    @Transactional
    fun save(mealStore: MealStore, componentStore: MealComponentStore, supplementIntakeStore: SupplementIntakeStore) {
        try {
            mealStore.save(this)
            mealComponents.forEach { componentStore.save(it) }
            supplementIntakes.forEach { supplementIntakeStore.save(it) }
        } catch (e: JpaObjectRetrievalFailureException) {
            throw NotFoundError.becauseNoMealFound(this.id.value.toString(), e)
        }
    }
}

class MealComponent(
    val id: UUID,
    val coachId: UUID,
    val clientId: UUID,
    val planId: UUID,
    val mealId: UUID,
    val food: FoodId,
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

