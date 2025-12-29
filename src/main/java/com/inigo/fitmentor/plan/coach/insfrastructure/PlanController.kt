package com.inigo.fitmentor.plan.coach.insfrastructure

import com.inigo.arch.spring.BearerService
import com.inigo.fitmentor.plan.coach.application.GetPlanById
import com.inigo.fitmentor.plan.coach.application.GetPlans
import com.inigo.fitmentor.plan.coach.application.UpdatePlan
import com.inigo.fitmentor.plan.coach.model.Plan
import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.PlanId
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@RestController
@RequestMapping("/api/user/plans")
@Validated
class PlanController(val getPlans: GetPlans,
                     val updatePlan: UpdatePlan,
                     val getPlanById: GetPlanById,
                     val bearerService: BearerService) {

        @GetMapping("/client/{clientId}")
    fun getPlan(@NotNull @PathVariable("clientId") clientId: UUID,
                @RequestHeader("Authorization") token: String): ResponseEntity<List<PlanResponse>> {
        val coachId = bearerService.parseToken(token).coachId
        LOG.debug("REST request to get plans for coach : {} and client: {}", coachId, clientId)
        val plans = getPlans.execute(ClientId(clientId), CoachId(UUID.fromString(coachId)))
        return if (plans.isEmpty()) {
            ResponseEntity.notFound().build()
        } else {
            val res = plans.map {
                PlanResponse(
                    id = it.id.value,
                    active = it.active,
                    client = it.client.value,
                    coach = it.coach.value,
                    description = it.description,
                    type = it.type,
                    goals = it.goals,
                    equipment = it.equipment,
                    startDate = it.startDate,
                    endDate = it.endDate
                )
            }.toList()
            ResponseEntity.ok(res)
        }
    }

    @GetMapping("/client/{clientId}/plan/{planId}")
    fun getPlan(@NotNull @PathVariable("clientId") clientId: UUID,
                @NotNull @PathVariable("planId") planId: UUID,
                @RequestHeader("Authorization") token: String): ResponseEntity<PlanResponse> {
        val coachId = bearerService.parseToken(token).coachId
        LOG.debug("REST request to get plan $planId for coach : $coachId and client: $clientId")
        val plan = getPlanById
            .execute(ClientId(clientId), CoachId(UUID.fromString(coachId)), PlanId(planId)) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(
            PlanResponse(
                id = plan.id.value,
                active = plan.active,
                client = plan.client.value,
                coach = plan.coach.value,
                description = plan.description,
                type = plan.type,
                goals = plan.goals,
                equipment = plan.equipment,
                startDate = plan.startDate,
                endDate = plan.endDate
                )
            )
        }

    @PutMapping()
    fun modifyPlan(@Valid @RequestBody planRequest: PlanModificationRequest): ResponseEntity<*> {
        LOG.debug("REST request to update plan of coach : {} and client : {}", planRequest.coach, planRequest.client)
        updatePlan.execute(toDomain(planRequest))
        return ResponseEntity.ok("")
    }

    fun toDomain(clientReq: PlanModificationRequest): Plan {
        return Plan(
            id = PlanId(clientReq.id),
            active = clientReq.active ?: true,
            client = ClientId(clientReq.client),
            coach = CoachId(clientReq.coach),
            description = clientReq.description ?: "",
            type = clientReq.type ?: "",
            goals = clientReq.goals ?: "",
            equipment = clientReq.equipment ?: "",
            startDate = clientReq.startDate ?: Instant.now(),
            endDate = clientReq.endDate ?: Instant.now().plus(30, ChronoUnit.DAYS),
            updatedAt = Instant.now()
        )
    }

    data class PlanResponse(
        val id: UUID,
        val active: Boolean,
        val client: UUID,
        val coach: UUID,
        val description: String,
        val type: String,
        val goals: String,
        val equipment: String,
        val startDate: Instant,
        val endDate: Instant
    )

    data class PlanModificationRequest(
        @field:NotNull(message = "id must not be null") var id: UUID,
        var active: Boolean? = true,
        @field:NotNull(message = "client must not be null") var client: UUID,
        @field:NotNull(message = "coach must not be null") var coach: UUID,
        var description: String? = null,
        var type: String? = null,
        var goals: String? = null,
        var equipment: String? = null,
        var startDate: Instant? = null,
        var endDate: Instant? = null
    )

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(PlanController::class.java)
    }
}