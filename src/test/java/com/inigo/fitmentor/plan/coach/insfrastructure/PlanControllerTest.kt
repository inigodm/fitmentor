package com.inigo.fitmentor.plan.coach.insfrastructure

import com.inigo.arch.ArchApplication
import com.inigo.arch.spring.BearerService
import com.inigo.arch.spring.LoggedInUser
import com.inigo.arch.user.infrastructure.TestSecurityConfig
import com.inigo.fitmentor.plan.coach.application.GetPlanById
import com.inigo.fitmentor.plan.coach.application.GetPlans
import com.inigo.fitmentor.plan.coach.application.UpdatePlan
import com.inigo.fitmentor.plan.coach.model.Plan
import com.inigo.fitmentor.shared.domain.ClientId
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.PlanId
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
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
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ArchApplication::class]
)
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
@AutoConfigureMockMvc
class PlanControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean(relaxed = true)
    private lateinit var getPlans: GetPlans

    @MockkBean(relaxed = true)
    private lateinit var getPlanById: GetPlanById

    @MockkBean(relaxed = true)
    private lateinit var updatePlan: UpdatePlan

    @MockkBean
    private lateinit var bearerService: BearerService

    @Test
    fun `should get plans for client and coach successfully`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val planId1 = UUID.randomUUID()
        val planId2 = UUID.randomUUID()
        val token = "valid-token"
        val startDate = Instant.now()
        val endDate = startDate.plus(30, ChronoUnit.DAYS)

        val plan1 = Plan(
            id = PlanId(planId1),
            active = true,
            client = ClientId(clientId),
            coach = CoachId(coachId),
            description = "Strength training",
            type = "Strength",
            goals = "Build muscle",
            equipment = "Dumbbells",
            startDate = startDate,
            endDate = endDate,
            updatedAt = Instant.now()
        )

        val plan2 = Plan(
            id = PlanId(planId2),
            active = true,
            client = ClientId(clientId),
            coach = CoachId(coachId),
            description = "Cardio training",
            type = "Cardio",
            goals = "Lose weight",
            equipment = "Treadmill",
            startDate = startDate,
            endDate = endDate,
            updatedAt = Instant.now()
        )

        every { bearerService.parseToken(any()) } returns LoggedInUser(
            name = "coachuser",
            email = "coach@example.com",
            id = userId,
            userRole = 1,
            clientId = null,
            coachId = coachId.toString()
        )
        every { getPlans.execute(ClientId(clientId), CoachId(coachId)) } returns listOf(plan1, plan2)

        // When & Then
        mockMvc.perform(
            get("/api/user/plans/client/{clientId}", clientId)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(planId1.toString()))
            .andExpect(jsonPath("$[0].client").value(clientId.toString()))
            .andExpect(jsonPath("$[0].coach").value(coachId.toString()))
            .andExpect(jsonPath("$[0].description").value("Strength training"))
            .andExpect(jsonPath("$[0].type").value("Strength"))
            .andExpect(jsonPath("$[0].goals").value("Build muscle"))
            .andExpect(jsonPath("$[0].equipment").value("Dumbbells"))
            .andExpect(jsonPath("$[0].active").value(true))
            .andExpect(jsonPath("$[1].id").value(planId2.toString()))
            .andExpect(jsonPath("$[1].description").value("Cardio training"))

        verify(atLeast = 1) { bearerService.parseToken(any()) }
        verify(exactly = 1) { getPlans.execute(ClientId(clientId), CoachId(coachId)) }
    }

    @Test
    fun `should return 404 when no plans exist for client and coach`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val token = "valid-token"

        every { bearerService.parseToken(any()) } returns LoggedInUser(
            name = "coachuser",
            email = "coach@example.com",
            id = userId,
            userRole = 1,
            clientId = null,
            coachId = coachId.toString()
        )
        every { getPlans.execute(ClientId(clientId), CoachId(coachId)) } returns emptyList()

        // When & Then
        mockMvc.perform(
            get("/api/user/plans/client/{clientId}", clientId)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isNotFound)

        verify(atLeast = 1) { bearerService.parseToken(any()) }
        verify(exactly = 1) { getPlans.execute(ClientId(clientId), CoachId(coachId)) }
    }

    @Test
    fun `should get specific plan by id successfully`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val planId = UUID.randomUUID()
        val token = "valid-token"
        val startDate = Instant.now()
        val endDate = startDate.plus(30, ChronoUnit.DAYS)

        val plan = Plan(
            id = PlanId(planId),
            active = true,
            client = ClientId(clientId),
            coach = CoachId(coachId),
            description = "HIIT training",
            type = "HIIT",
            goals = "Improve endurance",
            equipment = "None",
            startDate = startDate,
            endDate = endDate,
            updatedAt = Instant.now()
        )

        every { bearerService.parseToken(any()) } returns LoggedInUser(
            name = "coachuser",
            email = "coach@example.com",
            id = userId,
            userRole = 1,
            clientId = null,
            coachId = coachId.toString()
        )
        every { getPlanById.execute(ClientId(clientId), CoachId(coachId), PlanId(planId)) } returns plan

        // When & Then
        mockMvc.perform(
            get("/api/user/plans/client/{clientId}/plan/{planId}", clientId, planId)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(planId.toString()))
            .andExpect(jsonPath("$.client").value(clientId.toString()))
            .andExpect(jsonPath("$.coach").value(coachId.toString()))
            .andExpect(jsonPath("$.description").value("HIIT training"))
            .andExpect(jsonPath("$.type").value("HIIT"))
            .andExpect(jsonPath("$.goals").value("Improve endurance"))
            .andExpect(jsonPath("$.equipment").value("None"))
            .andExpect(jsonPath("$.active").value(true))

        verify(atLeast = 1) { bearerService.parseToken(any()) }
        verify(exactly = 1) { getPlanById.execute(ClientId(clientId), CoachId(coachId), PlanId(planId)) }
    }

    @Test
    fun `should return 404 when plan does not exist`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val planId = UUID.randomUUID()
        val token = "valid-token"

        every { bearerService.parseToken(any()) } returns LoggedInUser(
            name = "coachuser",
            email = "coach@example.com",
            id = userId,
            userRole = 1,
            clientId = null,
            coachId = coachId.toString()
        )
        every { getPlanById.execute(ClientId(clientId), CoachId(coachId), PlanId(planId)) } returns null

        // When & Then
        mockMvc.perform(
            get("/api/user/plans/client/{clientId}/plan/{planId}", clientId, planId)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isNotFound)

        verify(atLeast = 1) { bearerService.parseToken(any()) }
        verify(exactly = 1) { getPlanById.execute(ClientId(clientId), CoachId(coachId), PlanId(planId)) }
    }

    @Test
    fun `should update plan successfully with all fields`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val planId = UUID.randomUUID()
        val startDate = Instant.parse("2025-01-01T00:00:00Z")
        val endDate = Instant.parse("2025-01-31T00:00:00Z")

        justRun { updatePlan.execute(any()) }

        val requestBody = """
            {
                "id": "$planId",
                "active": true,
                "client": "$clientId",
                "coach": "$coachId",
                "description": "Updated description",
                "type": "Strength",
                "goals": "Build muscle mass",
                "equipment": "Dumbbells, Barbell",
                "startDate": "$startDate",
                "endDate": "$endDate"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            put("/api/user/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))

        verify(exactly = 1) { updatePlan.execute(any()) }
    }

    @Test
    fun `should update plan successfully with only required fields`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val planId = UUID.randomUUID()

        justRun { updatePlan.execute(any()) }

        val requestBody = """
            {
                "id": "$planId",
                "client": "$clientId",
                "coach": "$coachId"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            put("/api/user/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))

        verify(exactly = 1) { updatePlan.execute(any()) }
    }

    @Test
    fun `should return 400 when updating plan with missing id`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()

        val requestBody = """
            {
                "client": "$clientId",
                "coach": "$coachId",
                "description": "Description"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            put("/api/user/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)

        verify(exactly = 0) { updatePlan.execute(any()) }
    }

    @Test
    fun `should return 400 when updating plan with missing client`() {
        // Given
        val coachId = UUID.randomUUID()
        val planId = UUID.randomUUID()

        val requestBody = """
            {
                "id": "$planId",
                "coach": "$coachId",
                "description": "Description"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            put("/api/user/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)

        verify(exactly = 0) { updatePlan.execute(any()) }
    }

    @Test
    fun `should return 400 when updating plan with missing coach`() {
        // Given
        val clientId = UUID.randomUUID()
        val planId = UUID.randomUUID()

        val requestBody = """
            {
                "id": "$planId",
                "client": "$clientId",
                "description": "Description"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            put("/api/user/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)

        verify(exactly = 0) { updatePlan.execute(any()) }
    }

    @Test
    fun `should update plan with inactive status`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val planId = UUID.randomUUID()

        justRun { updatePlan.execute(any()) }

        val requestBody = """
            {
                "id": "$planId",
                "active": false,
                "client": "$clientId",
                "coach": "$coachId"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            put("/api/user/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))

        verify(exactly = 1) { updatePlan.execute(any()) }
    }

    @Test
    fun `should get single plan when only one exists`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val planId = UUID.randomUUID()
        val token = "valid-token"
        val startDate = Instant.now()
        val endDate = startDate.plus(30, ChronoUnit.DAYS)

        val plan = Plan(
            id = PlanId(planId),
            active = true,
            client = ClientId(clientId),
            coach = CoachId(coachId),
            description = "Single plan",
            type = "Mixed",
            goals = "General fitness",
            equipment = "Bodyweight",
            startDate = startDate,
            endDate = endDate,
            updatedAt = Instant.now()
        )

        every { bearerService.parseToken(any()) } returns LoggedInUser(
            name = "coachuser",
            email = "coach@example.com",
            id = userId,
            userRole = 1,
            clientId = null,
            coachId = coachId.toString()
        )
        every { getPlans.execute(ClientId(clientId), CoachId(coachId)) } returns listOf(plan)

        // When & Then
        mockMvc.perform(
            get("/api/user/plans/client/{clientId}", clientId)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(planId.toString()))
            .andExpect(jsonPath("$[0].description").value("Single plan"))
            .andExpect(jsonPath("$[0].type").value("Mixed"))

        verify(atLeast = 1) { bearerService.parseToken(any()) }
        verify(exactly = 1) { getPlans.execute(ClientId(clientId), CoachId(coachId)) }
    }
}

