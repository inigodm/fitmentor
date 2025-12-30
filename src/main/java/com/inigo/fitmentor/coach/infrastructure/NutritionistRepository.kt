package com.inigo.fitmentor.coach.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NutritionistRepository : JpaRepository<NutritionistJpa, UUID>
