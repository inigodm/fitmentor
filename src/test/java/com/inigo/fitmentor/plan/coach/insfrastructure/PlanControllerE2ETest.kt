package com.inigo.fitmentor.plan.coach.insfrastructure

import com.inigo.arch.ArchApplication
import com.inigo.arch.spring.BearerService
import com.inigo.arch.user.infrastructure.TestSecurityConfig
import com.inigo.fitmentor.plan.coach.model.Plan
import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.PlanId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@SpringBootTest(classes = [ArchApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
@Transactional
class PlanControllerE2ETest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var planRepository: PlanRepository

    @Autowired
    private lateinit var bearerService: BearerService

    @BeforeEach
    fun setup() {
        planRepository.deleteAll()
    }

    @Test
    fun `E2E - should retrieve plans for client and coach successfully`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val planId1 = UUID.randomUUID()
        val planId2 = UUID.randomUUID()
        val startDate = Instant.now()
        val endDate = startDate.plus(30, ChronoUnit.DAYS)

        // Create plans in database
        val plan1 = PlanJpa.fromDomain(
            Plan(
                id = PlanId(planId1),
                active = true,
                client = ClientId(clientId),
                coach = CoachId(coachId),
                description = "Strength training plan",
                type = "Strength",
                goals = "Build muscle",
                equipment = "Dumbbells, Barbell",
                startDate = startDate,
                endDate = endDate,
                updatedAt = Instant.now()
            )
        )
        val plan2 = PlanJpa.fromDomain(
            Plan(
                id = PlanId(planId2),
                active = true,
                client = ClientId(clientId),
                coach = CoachId(coachId),
                description = "Cardio plan",
                type = "Cardio",
                goals = "Lose weight",
                equipment = "Treadmill",
                startDate = startDate,
                endDate = endDate,
                updatedAt = Instant.now()
            )
        )

        planRepository.save(plan1)
        planRepository.save(plan2)

        // Generate token for coach
        val token = bearerService.generateToken(
            username = "coachuser",
            email = "coach@example.com",
            id = userId,
            clientId = null,
            coachId = coachId,
            userRole = 1
        ).removePrefix("Bearer ")

        // When - Retrieve plans
        mockMvc.perform(
            get("/api/user/plans/client/{clientId}", clientId)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].client").value(clientId.toString()))
            .andExpect(jsonPath("$[0].coach").value(coachId.toString()))
            .andExpect(jsonPath("$[0].active").value(true))
    }

    @Test
    fun `E2E - should retrieve specific plan by id`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val planId = UUID.randomUUID()
        val startDate = Instant.now()
        val endDate = startDate.plus(30, ChronoUnit.DAYS)

        val plan = PlanJpa.fromDomain(
            Plan(
                id = PlanId(planId),
                active = true,
                client = ClientId(clientId),
                coach = CoachId(coachId),
                description = "HIIT training plan",
                type = "HIIT",
                goals = "Improve endurance",
                equipment = "None",
                startDate = startDate,
                endDate = endDate,
                updatedAt = Instant.now()
            )
        )

        planRepository.save(plan)

        // Generate token for coach
        val token = bearerService.generateToken(
            username = "coachuser",
            email = "coach@example.com",
            id = userId,
            clientId = null,
            coachId = coachId,
            userRole = 1
        ).removePrefix("Bearer ")

        // When - Retrieve specific plan
        mockMvc.perform(
            get("/api/user/plans/client/{clientId}/plan/{planId}", clientId, planId)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(planId.toString()))
            .andExpect(jsonPath("$.client").value(clientId.toString()))
            .andExpect(jsonPath("$.coach").value(coachId.toString()))
            .andExpect(jsonPath("$.description").value("HIIT training plan"))
            .andExpect(jsonPath("$.type").value("HIIT"))
            .andExpect(jsonPath("$.goals").value("Improve endurance"))
            .andExpect(jsonPath("$.equipment").value("None"))
            .andExpect(jsonPath("$.active").value(true))
    }

    @Test
    fun `E2E - should update plan successfully`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val planId = UUID.randomUUID()
        val startDate = Instant.now()
        val endDate = startDate.plus(30, ChronoUnit.DAYS)

        // Create initial plan
        val plan = PlanJpa.fromDomain(
            Plan(
                id = PlanId(planId),
                active = true,
                client = ClientId(clientId),
                coach = CoachId(coachId),
                description = "Initial plan",
                type = "Strength",
                goals = "Initial goals",
                equipment = "Initial equipment",
                startDate = startDate,
                endDate = endDate,
                updatedAt = Instant.now()
            )
        )

        planRepository.save(plan)

        val updateRequestBody = """
            {
                "id": "$planId",
                "active": false,
                "client": "$clientId",
                "coach": "$coachId",
                "description": "Updated plan description",
                "type": "Cardio",
                "goals": "Updated goals",
                "equipment": "Updated equipment",
                "startDate": "$startDate",
                "endDate": "$endDate"
            }
        """.trimIndent()

        // When - Update plan
        mockMvc.perform(
            put("/api/user/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequestBody)
        )
            .andExpect(status().isOk)

        // Then - Verify plan was updated
        val updatedPlan = planRepository.findById(planId).get()
        assert(updatedPlan.description == "Updated plan description")
        assert(updatedPlan.type == "Cardio")
        assert(updatedPlan.goals == "Updated goals")
        assert(updatedPlan.equipment == "Updated equipment")
        assert(updatedPlan.active == false)
    }

    @Test
    fun `E2E - should return 404 when no plans exist for client and coach`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        // Generate token for coach
        val token = bearerService.generateToken(
            username = "coachuser",
            email = "coach@example.com",
            id = userId,
            clientId = null,
            coachId = coachId,
            userRole = 1
        ).removePrefix("Bearer ")

        // When & Then
        mockMvc.perform(
            get("/api/user/plans/client/{clientId}", clientId)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isNotFound)
    }
}

