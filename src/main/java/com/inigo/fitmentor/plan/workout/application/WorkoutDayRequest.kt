package com.inigo.fitmentor.plan.workout.application

data class WorkoutDayRequest(
    val dayName: String,
    val order: Int,
    val exercises: List<ExerciseRequest>,
    val notes: String? = null
)
