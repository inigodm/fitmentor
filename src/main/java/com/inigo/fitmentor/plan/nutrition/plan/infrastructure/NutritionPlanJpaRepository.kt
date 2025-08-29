package com.inigo.fitmentor.plan.nutrition.plan.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface NutritionPlanJpaRepository : JpaRepository<NutritionPlanJpa, UUID>

