package com.inigo.fitmentor.plan.coach.insfrastructure

import com.inigo.fitmentor.plan.coach.model.Plan
import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.PlanId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "plans")
class PlanJpa {
    @Id
    @Column(name = "id", nullable = false)
    lateinit var id: UUID
    @Column(name = "active", nullable = false)
    var active: Boolean? = true
    @Column(name = "client")
    lateinit var client: UUID
    @Column(name = "coach")
    lateinit var  coach: UUID
    @Column(name = "description")
    lateinit var  description: String
    @Column(name = "type")
    lateinit var  type: String
    @Column(name = "goals")
    lateinit var  goals: String
    @Column(name = "equipment")
    lateinit var  equipment: String
    @Column(name = "start_date")
    lateinit var startDate: Instant
    @Column(name = "end_date")
    lateinit var endDate: Instant
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    lateinit var createdAt: Instant
    @Column(name = "updated_at")
    lateinit var updatedAt: Instant

    fun toDomain(): Plan {
        return Plan(
            id = PlanId(id),
            active = active ?: true,
            client = ClientId(client),
            coach = CoachId(coach),
            description = description,
            type = type,
            goals = goals,
            equipment = equipment,
            startDate = startDate,
            endDate = endDate,
            updatedAt = updatedAt
        )
    }

    companion object {
        @JvmStatic
        fun fromDomain(plan: Plan) = PlanJpa().apply {
            id = plan.id.value
            active = plan.active
            client = plan.client.value
            coach = plan.coach.value
            description = plan.description
            type = plan.type
            goals = plan.goals
            equipment = plan.equipment
            startDate = plan.startDate
            endDate = plan.endDate
            updatedAt = plan.updatedAt
        }
    }
}