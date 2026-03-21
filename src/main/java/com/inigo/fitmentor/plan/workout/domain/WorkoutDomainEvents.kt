package com.inigo.fitmentor.plan.workout.domain

import com.inigo.fitmentor.shared.domain.events.DomainEvent
import java.util.*

class WorkoutPlanCreated(
    name: String = "WorkoutPlanCreated",
    val workoutPlanId: WorkoutPlanId,
    val coachId: UUID
) : DomainEvent(name = name)

class WorkoutPlanUpdated(
    name: String = "WorkoutPlanUpdated",
    val workoutPlanId: WorkoutPlanId,
    val coachId: UUID
) : DomainEvent(name = name)

class WorkoutPlanDeleted(
    name: String = "WorkoutPlanDeleted",
    val workoutPlanId: WorkoutPlanId,
    val coachId: UUID
) : DomainEvent(name = name)
