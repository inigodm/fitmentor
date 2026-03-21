package com.inigo.fitmentor.plan.workout.application

data class WorkoutStepResponse(
    val repetitions: Int,
    val weight: Double? = null,
    val restSeconds: Int
)
