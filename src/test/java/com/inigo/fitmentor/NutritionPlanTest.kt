package com.inigo.fitmentor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.ObjectMapper
import com.inigo.arch.ArchApplication
import com.inigo.arch.spring.BearerService
import com.inigo.fitmentor.plan.nutrition.meat.infrastucture.FoodJpa
import com.inigo.fitmentor.plan.nutrition.meat.infrastucture.MealComponentJpa
import com.inigo.fitmentor.plan.nutrition.meat.infrastucture.MealJpa
import com.inigo.fitmentor.plan.nutrition.meat.infrastucture.SupplementIntakeJpa
import com.inigo.fitmentor.plan.nutrition.plan.infrastructure.NutritionPlanJpa
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [ArchApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class NutritionPlanTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var entityManager: EntityManager

    private lateinit var foodIdRice: UUID
    private lateinit var foodIdChicken: UUID
    private lateinit var plan: NutritionPlanJpa
    private lateinit var token: String
    private val mealId1: UUID = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        foodIdChicken = UUID.randomUUID()
        val chicken = FoodJpa(
            id = foodIdChicken,
            name = "Pollo",
            calPer100g = 120.0,
            proteinPer100g = 22.0,
            carbohydratePer100g = 0.0,
            fatPer100g = 2.0
        )
        foodIdRice = UUID.randomUUID()
        val rice = FoodJpa(
            id = foodIdRice,
            name = "Arroz",
            calPer100g = 350.0,
            proteinPer100g = 7.0,
            carbohydratePer100g = 78.0,
            fatPer100g = 1.0
        )
        entityManager.persist(rice)
        entityManager.persist(chicken)

        plan = NutritionPlanJpa(
            id = UUID.randomUUID(),
            clientId = UUID.randomUUID(),
            coachId = UUID.randomUUID(),
            description = "Test plan",
            startDate = Instant.now(),
            endDate = Instant.now().plusSeconds(86400)
        )
        entityManager.persist(plan)

        val bearerService = BearerService("12345678901234567890123456789012")
        token = bearerService.generateToken(
            username = "test-user",
            email = "test@email.com",
            id = UUID.randomUUID(),
            clientId = UUID.randomUUID(),
            coachId = UUID.randomUUID(),
            userRole = 1
        )

        println("Generated token: $token")
    }

    @Test
    @Transactional
    fun `should create a new nutritional plan`() {

        val (planRequest, response) = createPlan()

        val plans = entityManager.createQuery("SELECT p FROM NutritionPlanJpa p", NutritionPlanJpa::class.java).resultList
        assert(plans.isNotEmpty())
        assertEquals("Test plan", plans.last().description)
        assertEquals(planRequest["clientId"], plans.last().clientId)
        assertEquals(planRequest["coachId"], plans.last().coachId)
    }

    @Test
    @Transactional
    fun `should add a meal to an existing nutritional plan`() {

        val (planRequest, response) = createPlan()
        val componentId1 = UUID.randomUUID()
        val mealRequest = mapOf(
            "id" to mealId1,
            "name" to "Rize meat",
            "components" to listOf(
                mapOf(
                    "id" to componentId1,
                    "mealId" to mealId1,
                    "planId" to plan.id,
                    "foodId" to foodIdRice,
                    "quantity" to 100.0,
                    "unit" to "GR"
                )
            ),
            "supplements" to emptyList<Any>()
        )
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/nutrition/plans/${plan.id}/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(mealRequest))
        ).andExpect(MockMvcResultMatchers.status().isOk)

        val plans = entityManager.createQuery("SELECT p FROM NutritionPlanJpa p", NutritionPlanJpa::class.java).resultList
        assert(plans.isNotEmpty())
        assertEquals("Test plan", plans.last().description)
        assertEquals(planRequest["clientId"], plans.last().clientId)
        assertEquals(planRequest["coachId"], plans.last().coachId)

        val meals = entityManager.createQuery("SELECT m FROM MealJpa m WHERE m.id = :mealId", MealJpa::class.java)
            .setParameter("mealId", mealId1)
            .resultList
        assert(meals.isNotEmpty())
        assertThat(meals.size).isEqualTo(1)
        assertThat(meals[0].planId).isEqualTo(plan.id)
        assertThat(meals[0].name).isEqualTo("Rize meat")

        val component = entityManager.createQuery("SELECT c FROM MealComponentJpa c WHERE c.id = :componentId", MealComponentJpa::class.java)
            .setParameter("componentId", componentId1)
            .singleResult
        assertEquals(foodIdRice, component.foodId)
        assertEquals(100.0, component.quantity)
        assertEquals(planRequest["clientId"], component.clientId)
        assertEquals(planRequest["coachId"], component.coachId)
        assertEquals("GR", component.unit)
    }

    @Test
    @Transactional
    @WithMockUser
    fun `should add supplementations`() {
        val supplementationRequest = mapOf(
            "supplementId" to UUID.randomUUID(),
            "supplementName" to "Creatina",
            "quantity" to 5.0,
            "unit" to "GRAM",
            "timeOfDay" to "Desayuno"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/nutrition/plans/${plan.id}/supplementations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplementationRequest))
        ).andExpect(MockMvcResultMatchers.status().isOk)

        val supplements = entityManager.createQuery("SELECT s FROM SupplementIntakeJpa s WHERE s.plan.id = :planId",
            SupplementIntakeJpa::class.java)
            .setParameter("planId", plan.id)
            .resultList
        assert(supplements.isNotEmpty())
        assertEquals("Creatina", supplements.first().supplementName)
        assertEquals(5.0, supplements.first().quantity)
    }

    @Test
    @Transactional
    fun `error adding unexixstent element`() {
        val mealRequest = mapOf(
            "name" to "Comida error",
            "components" to listOf(
                mapOf(
                    "foodId" to UUID.randomUUID(), // No existe en la BD
                    "foodName" to "Desconocido",
                    "quantity" to 100.0,
                    "unit" to "GRAM"
                )
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/nutrition/plans/${plan.id}/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mealRequest))
        ).andExpect(MockMvcResultMatchers.status().is5xxServerError)
    }

    fun createPlan(): Pair<Map<String, Any?>, MvcResult?> {
        val planRequest = mapOf(
            "clientId" to UUID.randomUUID(),
            "coachId" to UUID.randomUUID(),
            "description" to "Test plan",
            "meals" to listOf(
                mapOf(
                    "name" to "Comida 1",
                    "components" to listOf(
                        mapOf(
                            "foodId" to foodIdChicken,
                            "quantity" to 150.0,
                            "unit" to "GRAM"
                        )
                    )
                )
            ),
            "supplementIntakes" to emptyList<Any>(),
            "startDate" to Instant.now().toString(),
            "endDate" to Instant.now().plusSeconds(86400).toString()
        )

        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/nutrition/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(planRequest))
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        return Pair(planRequest, response)
    }
}

