package com.inigo.fitmentor.plan.nutrition.foods.infrastructure

import com.inigo.fitmentor.plan.nutrition.foods.domain.Food
import com.inigo.fitmentor.plan.nutrition.foods.domain.FoodStore
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class FoodJpaStore(val repository: FoodJpaRepository): FoodStore {

    @Cacheable("foods")
    override fun getAll(): List<Food> {
        return repository.findAll().map { it.toDomain() }
    }
}