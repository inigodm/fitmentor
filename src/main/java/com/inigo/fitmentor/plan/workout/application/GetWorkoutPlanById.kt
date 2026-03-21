package com.inigo.fitmentor.plan.workout.application

import com.inigo.fitmentor.plan.workout.domain.WorkoutPlanId
import com.inigo.fitmentor.plan.workout.domain.WorkoutStore
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetWorkoutPlanById(
    private val repository: WorkoutStore
) {
    fun execute(planId: UUID): WorkoutPlanResponse? {
        val plan = repository.findById(WorkoutPlanId(planId)) ?: return null

        val workoutDays = plan.workoutDays.map { day ->
            val exercises = day.exercises.map { exercise ->
                val series = exercise.series.map { s ->
                    val steps = s.steps.map { step ->
                        WorkoutStepResponse(
                            repetitions = step.repetitions,
                            weight = step.weight,
                            restSeconds = step.restSeconds
                        )
                    }
                    SeriesResponse(
                        order = s.order,
                        steps = steps,
                        rpe = s.rpe
                    )
                }
                ExerciseResponse(
                    id = exercise.id.value,
                    name = exercise.name,
                    description = exercise.description,
                    order = exercise.order,
                    series = series,
                    notes = exercise.notes
                )
            }
            WorkoutDayResponse(
                id = day.id.value,
                dayName = day.dayName,
                order = day.order,
                exercises = exercises,
                notes = day.notes
            )
        }

        return WorkoutPlanResponse(
            id = plan.id.value,
            name = plan.name,
            description = plan.description,
            durationWeeks = plan.durationWeeks,
            coachId = plan.coachId,
            workoutDays = workoutDays,
            createdAt = plan.createdAt,
            updatedAt = plan.updatedAt
        )
    }
}
