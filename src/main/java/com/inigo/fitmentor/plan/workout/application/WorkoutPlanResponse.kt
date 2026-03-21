package com.inigo.fitmentor.plan.workout.application

import java.time.Instant
import java.util.*

data class WorkoutPlanResponse(
    val id: UUID,
    val name: String,
    val description: String,
    val durationWeeks: Int,
    val coachId: UUID,
    val workoutDays: List<WorkoutDayResponse>,
    val createdAt: Instant,
    val updatedAt: Instant
)
