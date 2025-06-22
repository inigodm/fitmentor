package com.inigo.fitmentor.coach.application

import com.inigo.fitmentor.client.domain.Client
import com.inigo.fitmentor.client.domain.ClientService
import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.coach.domain.CoachService
import org.springframework.stereotype.Service

@Service
class UpdateCoach(val store: CoachService) {
  fun execute(coach: Coach) {
    coach.ensureUserExists(store) // No se si es necesario
    coach.save(store)
    coach.publishEvents()
  }
}
