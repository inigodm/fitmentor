package com.inigo.fitmentor.timeslot.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.CoachId
import com.inigo.shared.domain.PlanId
import com.inigo.shared.domain.TimeSlotId
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

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
    val startTime: LocalTime,
    val endTime: LocalTime
) {

}