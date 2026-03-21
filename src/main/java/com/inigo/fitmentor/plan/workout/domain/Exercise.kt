package com.inigo.fitmentor.plan.workout.domain

import java.util.*

@JvmInline
value class ExerciseId(val value: UUID)

data class Exercise(
    val id: ExerciseId = ExerciseId(UUID.randomUUID()),
    val name: String,
    val description: String? = null,
    val order: Int,
    val series: List<Series>,
    val notes: String? = null
) {
    init {
        require(name.isNotBlank()) { "Exercise name cannot be blank" }
        require(order >= 1) { "Exercise order must be >= 1" }
        require(series.isNotEmpty()) { "Exercise must have at least one series" }
        require(series.sortedBy { it.order }.mapIndexed { index, s -> s.order == index + 1 }.all { it }) {
            "Series orders must be sequential starting from 1"
        }
    }
}
