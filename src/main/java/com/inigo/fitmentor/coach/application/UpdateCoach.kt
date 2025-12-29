package com.inigo.fitmentor.coach.application

import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.coach.domain.CoachService
import com.inigo.fitmentor.shared.infrastructure.UserService
import org.springframework.stereotype.Service

@Service
class UpdateCoach(val store: CoachService, val userService: UserService) {
  fun execute(coach: Coach) {
    coach.ensureUserExists(store, userService)
    coach.save(store)
    coach.publishEvents()
  }
}
