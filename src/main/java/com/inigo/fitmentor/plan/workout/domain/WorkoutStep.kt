package com.inigo.fitmentor.plan.workout.domain

data class WorkoutStep(
    val repetitions: Int,
    val weight: Double? = null,
    val restSeconds: Int
) {
    init {
        require(repetitions > 0) { "Repetitions must be greater than 0" }
        require(weight == null || weight > 0) { "Weight must be greater than 0 if provided" }
        require(restSeconds >= 0) { "Rest seconds cannot be negative" }
    }
}
