package com.inigo.fitmentor.plan.nutrition.foods.domain

interface FoodStore {
    fun getAll(): List<Food>
}