package com.inigo.fitmentor.plan.workout.domain

import java.util.*

interface WorkoutStore {
    fun save(workoutPlan: WorkoutPlan)
    fun update(workoutPlan: WorkoutPlan)
    fun findById(id: WorkoutPlanId): WorkoutPlan?
    fun findByCoachId(coachId: UUID): List<WorkoutPlan>
    fun delete(id: WorkoutPlanId)
}
