package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

import com.inigo.fitmentor.plan.nutrition.meat.domain.Meal
import com.inigo.fitmentor.plan.nutrition.meat.domain.MealStore
import org.springframework.stereotype.Component

@Component
class MealStoreJpa(val repository: MealJpaRepository): MealStore {
    override fun save(meal: Meal) {
        repository.save(MealJpa.from(meal))
    }
}