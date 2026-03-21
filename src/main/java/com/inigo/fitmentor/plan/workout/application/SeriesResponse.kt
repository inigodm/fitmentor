package com.inigo.fitmentor.plan.workout.application

data class SeriesResponse(
    val order: Int,
    val steps: List<WorkoutStepResponse>,
    val rpe: Int? = null
)
