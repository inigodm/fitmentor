package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

import com.inigo.fitmentor.plan.nutrition.meat.domain.MealComponent
import com.inigo.fitmentor.plan.nutrition.meat.domain.MealComponentStore
import org.springframework.stereotype.Component

@Component
class MealComponentStoreJpa(val repository: MealComponentJpaRepository): MealComponentStore {
    override fun save(mealComponent: MealComponent) {
        repository.save(MealComponentJpa.from(mealComponent))
    }
}