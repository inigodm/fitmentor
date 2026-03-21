package com.inigo.fitmentor.plan.workout.domain

data class Series(
    val order: Int,
    val steps: List<WorkoutStep>,
    val rpe: Int? = null
) {
    init {
        require(order >= 1) { "Series order must be >= 1" }
        require(steps.isNotEmpty()) { "Series must have at least one step" }
        require(rpe == null || (rpe >= 1 && rpe <= 10)) { "RPE must be between 1 and 10 if provided" }
    }
}
