package com.inigo.fitmentor.plan.nutrition.plan.infrastructure

import com.inigo.arch.ArchApplication
import com.inigo.arch.user.infrastructure.TestSecurityConfig
import com.inigo.fitmentor.plan.nutrition.plan.application.AddNutritionPlan
import com.inigo.fitmentor.plan.nutrition.plan.domain.NutritionPlan
import com.inigo.fitmentor.plan.nutrition.plan.domain.NutritionPlanId
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.Instant
import java.util.*

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ArchApplication::class]
)
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
@AutoConfigureMockMvc
class NutritionPlanControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean(relaxed = true)
    private lateinit var addNutritionPlan: AddNutritionPlan

    @Test
    fun `should create nutrition plan successfully`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val startDate = Instant.parse("2025-01-01T00:00:00Z")
        val endDate = Instant.parse("2025-01-31T00:00:00Z")

        val nutritionPlan = NutritionPlan(
            id = NutritionPlanId(UUID.randomUUID()),
            clientId = clientId,
            coachId = coachId,
            description = "Balanced diet plan",
            startDate = startDate,
            endDate = endDate
        )

        every { addNutritionPlan.execute(any()) } returns nutritionPlan

        val requestBody = """
            {
                "clientId": "$clientId",
                "coachId": "$coachId",
                "description": "Balanced diet plan",
                "startDate": "$startDate",
                "endDate": "$endDate"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/api/nutrition/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)

        verify(exactly = 1) { addNutritionPlan.execute(any()) }
    }

    @Test
    fun `should create nutrition plan with different dates`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val startDate = Instant.parse("2025-06-01T00:00:00Z")
        val endDate = Instant.parse("2025-12-31T00:00:00Z")

        val nutritionPlan = NutritionPlan(
            id = NutritionPlanId(UUID.randomUUID()),
            clientId = clientId,
            coachId = coachId,
            description = "Long-term nutrition plan",
            startDate = startDate,
            endDate = endDate
        )

        every { addNutritionPlan.execute(any()) } returns nutritionPlan

        val requestBody = """
            {
                "clientId": "$clientId",
                "coachId": "$coachId",
                "description": "Long-term nutrition plan",
                "startDate": "$startDate",
                "endDate": "$endDate"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/api/nutrition/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)

        verify(exactly = 1) { addNutritionPlan.execute(any()) }
    }

    @Test
    fun `should handle empty description in nutrition plan`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val startDate = Instant.parse("2025-01-01T00:00:00Z")
        val endDate = Instant.parse("2025-01-31T00:00:00Z")

        val nutritionPlan = NutritionPlan(
            id = NutritionPlanId(UUID.randomUUID()),
            clientId = clientId,
            coachId = coachId,
            description = "",
            startDate = startDate,
            endDate = endDate
        )

        every { addNutritionPlan.execute(any()) } returns nutritionPlan

        val requestBody = """
            {
                "clientId": "$clientId",
                "coachId": "$coachId",
                "description": "",
                "startDate": "$startDate",
                "endDate": "$endDate"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/api/nutrition/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)

        verify(exactly = 1) { addNutritionPlan.execute(any()) }
    }

    @Test
    fun `should create nutrition plan for different clients`() {
        // Given
        val client1Id = UUID.randomUUID()
        val client2Id = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val startDate = Instant.parse("2025-01-01T00:00:00Z")
        val endDate = Instant.parse("2025-01-31T00:00:00Z")

        val nutritionPlan1 = NutritionPlan(
            id = NutritionPlanId(UUID.randomUUID()),
            clientId = client1Id,
            coachId = coachId,
            description = "Plan for client 1",
            startDate = startDate,
            endDate = endDate
        )

        val nutritionPlan2 = NutritionPlan(
            id = NutritionPlanId(UUID.randomUUID()),
            clientId = client2Id,
            coachId = coachId,
            description = "Plan for client 2",
            startDate = startDate,
            endDate = endDate
        )

        every { addNutritionPlan.execute(match { it.clientId == client1Id }) } returns nutritionPlan1
        every { addNutritionPlan.execute(match { it.clientId == client2Id }) } returns nutritionPlan2

        val requestBody1 = """
            {
                "clientId": "$client1Id",
                "coachId": "$coachId",
                "description": "Plan for client 1",
                "startDate": "$startDate",
                "endDate": "$endDate"
            }
        """.trimIndent()

        val requestBody2 = """
            {
                "clientId": "$client2Id",
                "coachId": "$coachId",
                "description": "Plan for client 2",
                "startDate": "$startDate",
                "endDate": "$endDate"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/api/nutrition/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1)
        )
            .andExpect(status().isOk)

        mockMvc.perform(
            post("/api/nutrition/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2)
        )
            .andExpect(status().isOk)

        verify(exactly = 1) { addNutritionPlan.execute(match { it.clientId == client1Id }) }
        verify(exactly = 1) { addNutritionPlan.execute(match { it.clientId == client2Id }) }
    }
}

