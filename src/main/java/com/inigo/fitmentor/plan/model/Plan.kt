package com.inigo.fitmentor.plan.model

import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.PlanId
import java.time.Instant

data class Plan(
    val id: PlanId,
    val active: Boolean,
    val client: ClientId,
    val coach: CoachId,
    val description: String,
    val type: String,
    val goals: String,
    val equipment: String,
    val startDate: Instant,
    val endDate: Instant,
    val updatedAt: Instant) {

    fun save(store: PlanStore) {
        store.save(this)
    }
}