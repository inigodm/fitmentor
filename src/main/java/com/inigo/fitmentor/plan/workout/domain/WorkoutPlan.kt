package com.inigo.fitmentor.plan.workout.domain

import java.util.*
import java.time.Instant

@JvmInline
value class WorkoutPlanId(val value: UUID)

class WorkoutPlan(
    val id: WorkoutPlanId = WorkoutPlanId(UUID.randomUUID()),
    val name: String,
    val description: String,
    val durationWeeks: Int,
    val coachId: UUID,
    val workoutDays: List<WorkoutDay>,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
) {
    init {
        require(name.isNotBlank()) { "Workout plan name cannot be blank" }
        require(description.isNotBlank()) { "Workout plan description cannot be blank" }
        require(durationWeeks > 0) { "Duration weeks must be greater than 0" }
        require(workoutDays.isNotEmpty()) { "Workout plan must have at least one day" }
        require(workoutDays.size <= 7) { "Workout plan cannot have more than 7 days" }
        require(workoutDays.sortedBy { it.order }.mapIndexed { index, d -> d.order == index + 1 }.all { it }) {
            "Workout day orders must be sequential starting from 1"
        }
    }

    fun update(repository: WorkoutStore) {
        repository.update(this)
    }
}
