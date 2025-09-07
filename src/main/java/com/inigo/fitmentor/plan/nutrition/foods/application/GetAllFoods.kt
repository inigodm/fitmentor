package com.inigo.fitmentor.plan.nutrition.foods.application

import com.inigo.fitmentor.plan.nutrition.foods.domain.Food
import com.inigo.fitmentor.plan.nutrition.foods.domain.FoodStore
import org.springframework.stereotype.Component

@Component
class GetAllFoods(var store: FoodStore) {

    fun execute() : List<Food> {
        return store.getAll()
    }
}