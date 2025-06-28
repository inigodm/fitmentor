package com.inigo.fitmentor.coach.application

import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientService
import com.inigo.fitmentor.client.infrastructure.ClientController
import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.coach.domain.CoachService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateCoach(val store: CoachService) {
  fun execute(coach: Coach) {
    if (coach.alreadyExists(store)) {
      println("Coach already exists ID: ${coach.id}")
      return
    }
    coach.ensureUserExists(store)
    coach.create(store)
    coach.publishEvents()
  }

  companion object {
    private val LOG: Logger = LoggerFactory.getLogger(CreateCoach::class.java)
  }
}
