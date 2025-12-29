package com.inigo.fitmentor.plan.nutrition.foods.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FoodJpaRepository: JpaRepository<FoodJpa, UUID>