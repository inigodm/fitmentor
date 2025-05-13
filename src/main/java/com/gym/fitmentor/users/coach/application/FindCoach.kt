package com.gym.fitmentor.users.coach.application

import com.gym.fitmentor.users.coach.domain.Coach
import com.gym.fitmentor.users.coach.domain.CoachService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FindCoach(val store: CoachService) {
    /**
     * Find a coach by id
     * @param id the id of the coach
     * @return the coach if found, empty otherwise
     */
  fun execute(id: UUID): Coach? {
    return store.findOne(id).orElse(null)
  }
}
