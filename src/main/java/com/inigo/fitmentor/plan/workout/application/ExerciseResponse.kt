package com.inigo.fitmentor.plan.workout.application

import java.util.*

data class ExerciseResponse(
    val id: UUID,
    val name: String,
    val description: String? = null,
    val order: Int,
    val series: List<SeriesResponse>,
    val notes: String? = null
)
