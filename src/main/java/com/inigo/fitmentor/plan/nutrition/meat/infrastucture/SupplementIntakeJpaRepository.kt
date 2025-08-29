package com.inigo.fitmentor.plan.nutrition.meat.infrastucture

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SupplementIntakeJpaRepository : JpaRepository<SupplementIntakeJpa, UUID>

