package com.gym.fitmentor.users.coach.domain

import com.gym.fitmentor.users.client.domain.Client
import java.io.Serializable
import java.util.UUID

data class Coach(
  var id: UUID,
  var nutrition: Boolean,
  var fitness: Boolean,
  var experience: Int,
  var bio: String,
  var trainingStyle: String,
  var availableDays: String,
  var city: String,
  var worksOnline: Boolean,
  var worksInPerson: Boolean,
  var priceOnline: Int = -1,
  var priceonPerson: Int = -1,
  var certifcations: String,
  var user: String,
  var clients: MutableSet<Client>,
) : Serializable
