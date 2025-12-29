package com.inigo.fitmentor.plan.nutrition.foods.infrastructure

import com.inigo.arch.spring.LoggedInUser
import com.inigo.fitmentor.plan.nutrition.foods.application.GetAllFoods
import com.inigo.fitmentor.plan.nutrition.foods.domain.Food
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/nutrition/foods")
class FoodController(val getAllFoods: GetAllFoods) {
    @GetMapping
    fun getAllFoods(): List<Food> {
        //val loggedInUser = SecurityContextHolder.getContext().authentication.principal as LoggedInUser
        return getAllFoods.execute()
    }
}