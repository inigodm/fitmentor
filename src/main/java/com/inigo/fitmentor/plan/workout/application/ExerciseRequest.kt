package com.inigo.fitmentor.plan.workout.application

data class ExerciseRequest(
    val name: String,
    val description: String? = null,
    val order: Int,
    val series: List<SeriesRequest>,
    val notes: String? = null
)
