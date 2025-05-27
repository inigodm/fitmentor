package com.inigo.fitmentor.client.domain

import jakarta.validation.constraints.NotNull
import java.io.Serializable
import java.util.UUID

data class Client(
  @field:NotNull(message = "id must not be null") var id: UUID,
  var goals: String? = null,
  var age: Int? = null,
  var injuries: String? = null,
  var weight: Int? = null,
  var equipmentAccess: Int? = null,
  var preferedTrainingStyle: String? = null,
  var phonenumber: String? = null,
  @field:NotNull(message = "user must not be null") var user: UUID,
  var coach: UUID? = null
) : Serializable
