package com.inigo.fitmentor.timeslot.application

import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.PlanId
import com.inigo.fitmentor.shared.domain.TimeSlotId
import java.time.DayOfWeek
import java.time.LocalTime

class CreateTimeSlot {
    fun execute(id: TimeSlotId,
                clientId: ClientId,
                coachId: CoachId,
                planId: PlanId,
                dayOfWeek: DayOfWeek,
                startTime: LocalTime,
                endTime: LocalTime) {
        // Logic to create a time slot
        // This could involve saving the time slot to a database or performing some business logic
        // For example:
        // timeSlotStore.save(timeSlot)
    }
}