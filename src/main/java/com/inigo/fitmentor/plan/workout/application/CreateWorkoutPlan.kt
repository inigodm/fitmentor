package com.inigo.fitmentor.plan.workout.application

import com.inigo.fitmentor.plan.workout.domain.*
import org.springframework.stereotype.Component

@Component
class CreateWorkoutPlan(
    private val repository: WorkoutStore
) {
    fun execute(request: CreateWorkoutPlanRequest): WorkoutPlan {
        val workoutDays = request.workoutDays.map { dayRequest ->
            val exercises = dayRequest.exercises.map { exerciseRequest ->
                val series = exerciseRequest.series.map { seriesRequest ->
                    val steps = seriesRequest.steps.map { stepRequest ->
                        WorkoutStep(
                            repetitions = stepRequest.repetitions,
                            weight = stepRequest.weight,
                            restSeconds = stepRequest.restSeconds
                        )
                    }
                    Series(
                        order = seriesRequest.order,
                        steps = steps,
                        rpe = seriesRequest.rpe
                    )
                }
                Exercise(
                    name = exerciseRequest.name,
                    description = exerciseRequest.description,
                    order = exerciseRequest.order,
                    series = series,
                    notes = exerciseRequest.notes
                )
            }
            WorkoutDay(
                dayName = dayRequest.dayName,
                order = dayRequest.order,
                exercises = exercises,
                notes = dayRequest.notes
            )
        }

        val plan = WorkoutPlan(
            name = request.name,
            description = request.description,
            durationWeeks = request.durationWeeks,
            coachId = request.coachId,
            workoutDays = workoutDays
        )

        repository.save(plan)
        return plan
    }
}
