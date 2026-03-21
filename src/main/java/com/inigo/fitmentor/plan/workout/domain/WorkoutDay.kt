package com.inigo.fitmentor.plan.workout.domain

import java.util.*

@JvmInline
value class WorkoutDayId(val value: UUID)

data class WorkoutDay(
    val id: WorkoutDayId = WorkoutDayId(UUID.randomUUID()),
    val dayName: String,
    val order: Int,
    val exercises: List<Exercise>,
    val notes: String? = null
) {
    init {
        require(dayName.isNotBlank()) { "Day name cannot be blank" }
        require(order >= 1) { "Day order must be >= 1" }
        require(exercises.isNotEmpty()) { "Day must have at least one exercise" }
        require(exercises.sortedBy { it.order }.mapIndexed { index, e -> e.order == index + 1 }.all { it }) {
            "Exercise orders must be sequential starting from 1"
        }
    }
}
