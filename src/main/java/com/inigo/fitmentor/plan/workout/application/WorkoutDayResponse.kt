package com.inigo.fitmentor.plan.workout.application

import java.util.*

data class WorkoutDayResponse(
    val id: UUID,
    val dayName: String,
    val order: Int,
    val exercises: List<ExerciseResponse>,
    val notes: String? = null
)
