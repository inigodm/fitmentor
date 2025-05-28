package com.inigo.fitmentor.plan.model

import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.CoachId
import com.inigo.shared.domain.PlanId
import com.inigo.shared.domain.TimeSlotId
import java.time.LocalDate
import java.time.LocalDateTime

data class Plan(
    val id: PlanId,
    val active: Boolean,
    var timeSlots: List<TimeSlotId> = mutableListOf(),
    val client: ClientId,
    val coach: CoachId,
    val description: String,
    val type: String,
    val equipment: String,
    val goal: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime) {

    fun save(store: PlanStore) {
        store.save(this)
    }
}