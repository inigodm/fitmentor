package com.inigo.fitmentor.plan.workout.application

data class SeriesRequest(
    val order: Int,
    val steps: List<WorkoutStepRequest>,
    val rpe: Int? = null
)
