package com.inigo.fitmentor.coach.application

import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.coach.domain.CoachService
import org.springframework.stereotype.Service

@Service
class FindAllCoaches(private val coachService: CoachService) {
    fun execute(): List<Coach> {
        return coachService.findAll()
    }
}
