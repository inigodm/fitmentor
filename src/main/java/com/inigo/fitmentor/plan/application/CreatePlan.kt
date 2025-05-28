package com.inigo.fitmentor.plan.application

import com.inigo.fitmentor.plan.model.Plan
import com.inigo.fitmentor.plan.model.PlanStore
import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.CoachId
import com.inigo.shared.domain.PlanId
import com.inigo.shared.domain.TimeSlotId
import java.time.LocalDate
import java.time.LocalDateTime

class CreatePlan(val store: PlanStore) {

    fun execute(createPlanCommand: CreatePlanCommand): Plan {
        val plan = Plan(
            id = createPlanCommand.planId,
            active = true,
            timeSlots = createPlanCommand.timeSlots,
            client = createPlanCommand.client,
            coach = createPlanCommand.coach,
            description = createPlanCommand.description,
            type = createPlanCommand.type,
            equipment = createPlanCommand.equipment,
            goal = createPlanCommand.goal,
            startDate = createPlanCommand.startDate,
            endDate = createPlanCommand.endDate,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        plan.save(store)
        return plan

    }
}

data class CreatePlanCommand(
    val planId: PlanId,
    val timeSlots: List<TimeSlotId>,
    val client: ClientId,
    val coach: CoachId,
    val description: String,
    val type: String,
    val equipment: String,
    val goal: String,
    val startDate: LocalDate,
    val endDate: LocalDate)