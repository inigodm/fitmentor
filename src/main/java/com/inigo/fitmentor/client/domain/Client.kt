package com.inigo.fitmentor.client.domain

import java.io.Serializable
import java.util.UUID

data class Client(
  var id: UUID,
  var goals: String? = null,
  var age: Int? = null,
  var injuries: String? = null,
  var weight: Int? = null,
  var equipmentAccess: Int? = null,
  var preferedTrainingStyle: String? = null,
  var phonenumber: String? = null,
  var user: UUID,
  var coach: UUID? = null
) : Serializable
