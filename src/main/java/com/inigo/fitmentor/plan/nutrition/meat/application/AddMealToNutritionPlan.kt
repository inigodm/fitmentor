package com.inigo.fitmentor.plan.nutrition.meat.application

import com.inigo.fitmentor.plan.nutrition.meat.domain.FoodId
import com.inigo.fitmentor.plan.nutrition.meat.domain.Meal
import com.inigo.fitmentor.plan.nutrition.meat.domain.MealComponent
import com.inigo.fitmentor.plan.nutrition.meat.domain.MealComponentStore
import com.inigo.fitmentor.plan.nutrition.meat.domain.MealId
import com.inigo.fitmentor.plan.nutrition.meat.domain.MealStore
import com.inigo.fitmentor.plan.nutrition.meat.domain.SupplementId
import com.inigo.fitmentor.plan.nutrition.meat.domain.SupplementIntake
import com.inigo.fitmentor.plan.nutrition.meat.domain.SupplementIntakeStore
import com.inigo.fitmentor.plan.nutrition.meat.domain.UnitType
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AddMealToNutritionPlan(
    private val mealStore: MealStore,
    private val componentStore: MealComponentStore,
    private val supplementIntakeStore: SupplementIntakeStore
) {
    fun execute(request: AddMealToNutritionPlanRequest){
        val meal = Meal(
            id = MealId(request.meal.id),
            name = request.meal.name,
            planId = request.planId,
            mealComponents = request.meal.components.map {
                MealComponent(
                    id = it.id,
                    coachId = request.coachId,
                    clientId = request.clientId,
                    planId = request.planId,
                    food = FoodId(it.foodId),
                    mealId = it.mealId,
                    quantity = it.quantity,
                    unit = UnitType.valueOf(it.unit)
                )
            },
            supplementIntakes = request.meal.supplements.map {
                SupplementIntake(
                    id = it.id,
                    coachId = request.coachId,
                    clientId = request.clientId,
                    planId = request.planId,
                    supplementId = SupplementId(it.supplementId),
                    mealId = it.mealId,
                    quantity = it.quantity,
                    unit = UnitType.valueOf(it.unit)
                )
            }
        )
        meal.save(mealStore, componentStore, supplementIntakeStore)
    }
}

data class AddMealToNutritionPlanRequest(
    val planId: UUID,
    val coachId: UUID,
    val clientId: UUID,
    val meal: MealRequest
)

data class MealRequest(
    val id: UUID,
    val name: String,
    val components: List<MealComponentRequest>,
    val supplements: List<SupplementIntakeRequest>
)

data class MealComponentRequest(
    val id: UUID,
    val mealId: UUID,
    val planId: UUID,
    val foodId: UUID,
    val quantity: Double,
    val unit: String
)

data class SupplementIntakeRequest(
    val id: UUID,
    val mealId: UUID,
    val planId: UUID,
    val supplementId: UUID,
    val quantity: Double,
    val unit: String
)