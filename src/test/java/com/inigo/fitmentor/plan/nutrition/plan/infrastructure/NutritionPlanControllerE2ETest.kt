package com.inigo.fitmentor.plan.nutrition.plan.infrastructure

import com.inigo.arch.ArchApplication
import com.inigo.arch.user.infrastructure.TestSecurityConfig
import org.junit.jupiter.api.BeforeEach
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
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [ArchApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
@Transactional
class NutritionPlanControllerE2ETest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var nutritionPlanRepository: NutritionPlanJpaRepository


    @BeforeEach
    fun setup() {
        nutritionPlanRepository.deleteAll()
    }

    @Test
    fun `E2E - should create a nutrition plan successfully`() {
        // Given
        val clientId = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val startDate = Instant.now()
        val endDate = startDate.plusSeconds(30 * 24 * 60 * 60) // 30 days later

        val createRequestBody = """
            {
                "clientId": "$clientId",
                "coachId": "$coachId",
                "description": "High protein diet for muscle gain",
                "startDate": "$startDate",
                "endDate": "$endDate"
            }
        """.trimIndent()

        // When - Create nutrition plan
        mockMvc.perform(
            post("/api/nutrition/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody)
        )
            .andExpect(status().isOk)

        // Then - Verify plan was created in database
        val plans = nutritionPlanRepository.findAll()
        assert(plans.size == 1)
        assert(plans[0].clientId == clientId)
        assert(plans[0].coachId == coachId)
        assert(plans[0].description == "High protein diet for muscle gain")
    }

    @Test
    fun `E2E - should create multiple nutrition plans for different clients`() {
        // Given
        val client1Id = UUID.randomUUID()
        val client2Id = UUID.randomUUID()
        val coachId = UUID.randomUUID()
        val startDate = Instant.now()
        val endDate = startDate.plusSeconds(30 * 24 * 60 * 60)

        val request1 = """
            {
                "clientId": "$client1Id",
                "coachId": "$coachId",
                "description": "Weight loss plan",
                "startDate": "$startDate",
                "endDate": "$endDate"
            }
        """.trimIndent()

        val request2 = """
            {
                "clientId": "$client2Id",
                "coachId": "$coachId",
                "description": "Maintenance plan",
                "startDate": "$startDate",
                "endDate": "$endDate"
            }
        """.trimIndent()

        // When - Create two nutrition plans
        mockMvc.perform(
            post("/api/nutrition/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request1)
        )
            .andExpect(status().isOk)

        mockMvc.perform(
            post("/api/nutrition/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request2)
        )
            .andExpect(status().isOk)

        // Then - Verify both plans were created
        val plans = nutritionPlanRepository.findAll()
        assert(plans.size == 2)
        assert(plans.any { it.clientId == client1Id && it.description == "Weight loss plan" })
        assert(plans.any { it.clientId == client2Id && it.description == "Maintenance plan" })
    }
}

