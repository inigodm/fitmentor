package com.inigo.fitmentor.coach.infrastructure.api.models

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.inigo.fitmentor.timeslot.domain.TimeSlot
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 
 * @param user User id
 * @param id Coach id
 * @param name Name of the coach.
 * @param location Location of the coach.
 * @param languages List of languages spoken by the coach.
 * @param physical Offers physical training services.
 * @param experienceYears 
 * @param bio Coach biography.
 * @param availability 
 * @param clients List of client ids associated with the coach.
 * @param nutritionist Offers nutritionist services.
 * @param trainer Offers trainer services.
 */
data class CoachSaveRequest(
    @field:NotNull(message = "user must not be null") val user: java.util.UUID,
    @field:NotNull(message = "id must not be null") val id: java.util.UUID,
    @field:NotNull(message = "name must not be null") val name: kotlin.String? = null,
    @field:NotNull(message = "location must not be null") val location: kotlin.String? = null,
    @field:NotNull(message = "languages must not be null") val languages: kotlin.collections.List<kotlin.String>? = null,
    @field:NotNull(message = "physical must not be null") val physical: kotlin.Boolean? = null,
    @field:NotNull(message = "experienceYears must not be null") val experienceYears: kotlin.Int? = null,
    @field:NotNull(message = "bio must not be null") val bio: kotlin.String? = null,
    @field:NotNull(message = "availability must not be null") val availability: kotlin.collections.List<TimeSlot>? = null,
    @field:NotNull(message = "clients must not be null") val clients: kotlin.collections.List<java.util.UUID>? = null,
    @field:NotNull(message = "nutritionist must not be null") val nutritionist: kotlin.Boolean? = null,
    @field:NotNull(message = "trainer must not be null") val trainer: kotlin.Boolean? = null
    )

