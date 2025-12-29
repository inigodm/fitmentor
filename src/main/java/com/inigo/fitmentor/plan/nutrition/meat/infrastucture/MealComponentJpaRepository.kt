package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MealComponentJpaRepository : JpaRepository<MealComponentJpa, UUID>

