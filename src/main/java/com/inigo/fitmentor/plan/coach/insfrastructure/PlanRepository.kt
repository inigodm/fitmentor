package com.inigo.fitmentor.plan.coach.insfrastructure

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PlanRepository: JpaRepository<PlanJpa, UUID> {
    fun findByClientAndCoach(clientId: UUID, coachId: UUID): List<PlanJpa>
}