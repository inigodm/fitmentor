package com.gym.fitmentor.users.coach.application

import com.gym.fitmentor.users.coach.domain.Coach
import com.gym.fitmentor.users.coach.domain.CoachService
import org.springframework.stereotype.Service

@Service
class UpdateCoach(val store: CoachService) {
  fun execute(coach: Coach) {
    store.save(coach)
  }
}
