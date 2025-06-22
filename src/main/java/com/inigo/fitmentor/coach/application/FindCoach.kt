package com.inigo.fitmentor.coach.application

import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.coach.domain.CoachService
import com.inigo.shared.domain.CoachId
import org.springframework.stereotype.Service

@Service
class FindCoach(val store: CoachService) {
  fun execute(id: CoachId): Coach? {
    return store.findByCoachId(id)
  }
}
