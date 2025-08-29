package com.inigo.fitmentor.plan.nutrition.plan.infrastructure

import com.inigo.fitmentor.plan.nutrition.plan.domain.NutritionPlan
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "nutrition_plans")
class NutritionPlanJpa(
    @Id
    @Column(name = "id", nullable = false)
    var id: UUID = UUID.randomUUID(),
    @Column(name = "client_id", nullable = false)
    var clientId: UUID,
    @Column(name = "coach_id", nullable = false)
    var coachId: UUID,
    @Column(name = "description")
    var description: String,
    @Column(name = "start_date")
    var startDate: Instant,
    @Column(name = "end_date")
    var endDate: Instant,
    @Column(name = "created_at")
    var createdAt: Instant = Instant.now(),
    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now()
) {
    constructor() : this(
        id = UUID.randomUUID(),
        clientId = UUID.randomUUID(),
        coachId = UUID.randomUUID(),
        description = "",
        startDate = Instant.now(),
        endDate = Instant.now()
    )
    companion object {
        fun from(
            nutrition: NutritionPlan
        ): NutritionPlanJpa {
            return NutritionPlanJpa(
                clientId = nutrition.clientId,
                coachId = nutrition.coachId,
                description = nutrition.description,
                startDate = nutrition.startDate,
                endDate = nutrition.endDate
            )
        }
    }
}


