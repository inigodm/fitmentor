package com.inigo.fitmentor.client.domain

import com.inigo.shared.domain.ClientId
import com.inigo.shared.domain.UserId
import jakarta.validation.constraints.NotNull
import java.io.Serializable
import java.util.UUID

data class Client(
  @field:NotNull(message = "id must not be null") var id: ClientId,
  var goals: String? = null,
  var age: Int? = null,
  var injuries: String? = null,
  var weight: Int? = null,
  var equipmentAccess: Int? = null,
  var phonenumber: String? = null,
  @field:NotNull(message = "user must not be null") var user: UserId,
  var plans: List<UUID>? = null
) : Serializable
