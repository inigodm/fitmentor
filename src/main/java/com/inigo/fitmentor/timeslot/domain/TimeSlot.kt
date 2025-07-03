package com.inigo.fitmentor.timeslot.domain

import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.PlanId
import com.inigo.fitmentor.shared.domain.TimeSlotId
import java.time.DayOfWeek
import java.time.Instant

/**
 *
 * @param client List of client ids associated with the coach.
 * @param coach List of client ids associated with the coach.
 * @param dayOfWeek
 * @param startTime
 * @param endTime
 */
data class TimeSlot(
    val id: TimeSlotId,
    val client: ClientId,
    val coach: CoachId,
    val plan: PlanId,
    val dayOfWeek: DayOfWeek,
    val startTime: Instant,
    val endTime: Instant
) {

}