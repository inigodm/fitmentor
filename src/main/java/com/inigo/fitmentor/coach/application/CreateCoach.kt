package com.inigo.fitmentor.coach.application

import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.coach.domain.CoachService
import com.inigo.fitmentor.shared.infrastructure.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateCoach(val store: CoachService, val userService: UserService) {
  fun execute(coach: Coach) {
      println("Creating coach ID: ${coach.id}")
    if (coach.alreadyExists(store)) {
      println("Coach already exists ID: ${coach.id}")
      return
    }
      println("Coach does not exist, proceeding to create ID: ${coach.id}")
    coach.ensureUserExists(store, userService)
      println("User ensured for coach ID: ${coach.id}")
    coach.create(store)
        println("Coach created in store ID: ${coach.id}")
    coach.publishEvents()
        println("Events published for coach ID: ${coach.id}")
  }
}
