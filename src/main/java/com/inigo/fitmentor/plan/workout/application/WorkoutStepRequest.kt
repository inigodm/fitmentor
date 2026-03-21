package com.inigo.fitmentor.plan.workout.application

data class WorkoutStepRequest(
    val repetitions: Int,
    val weight: Double? = null,
    val restSeconds: Int
)
