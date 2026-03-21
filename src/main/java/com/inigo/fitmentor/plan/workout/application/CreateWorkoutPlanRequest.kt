package com.inigo.fitmentor.plan.workout.application

import java.util.*

data class CreateWorkoutPlanRequest(
    val name: String,
    val description: String,
    val durationWeeks: Int,
    val coachId: UUID,
    val workoutDays: List<WorkoutDayRequest>
)
