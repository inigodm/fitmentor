package com.inigo.fitmentor.plan.nutrition.foods.infrastructure

import com.inigo.arch.ArchApplication
import com.inigo.arch.user.infrastructure.TestSecurityConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest(classes = [ArchApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
@Transactional
class FoodControllerE2ETest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var foodRepository: FoodJpaRepository


    @BeforeEach
    fun setup() {
        foodRepository.deleteAll()
    }

    @Test
    fun `E2E - should retrieve all foods successfully`() {
        // Given - Create some foods in the database
        val food1 = FoodJpa(
            id = UUID.randomUUID(),
            name = "Chicken Breast",
            calPer100g = 1650,  // 165.0 * 10
            proteinPer100g = 310,  // 31.0 * 10
            carbohydratePer100g = 0,
            fatPer100g = 36  // 3.6 * 10
        )
        val food2 = FoodJpa(
            id = UUID.randomUUID(),
            name = "Brown Rice",
            calPer100g = 1110,  // 111.0 * 10
            proteinPer100g = 26,  // 2.6 * 10
            carbohydratePer100g = 230,  // 23.0 * 10
            fatPer100g = 9  // 0.9 * 10
        )
        val food3 = FoodJpa(
            id = UUID.randomUUID(),
            name = "Broccoli",
            calPer100g = 340,  // 34.0 * 10
            proteinPer100g = 28,  // 2.8 * 10
            carbohydratePer100g = 70,  // 7.0 * 10
            fatPer100g = 4  // 0.4 * 10
        )

        foodRepository.save(food1)
        foodRepository.save(food2)
        foodRepository.save(food3)

        // When - Retrieve all foods
        mockMvc.perform(get("/api/nutrition/foods"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].name").exists())
            .andExpect(jsonPath("$[0].calPer100g").exists())
            .andExpect(jsonPath("$[0].proteinPer100g").exists())
            .andExpect(jsonPath("$[0].carbohydratePer100g").exists())
            .andExpect(jsonPath("$[0].fatPer100g").exists())
    }

    @Test
    fun `E2E - should return empty list when no foods exist`() {
        // Given - Empty database (cleared in setup)

        // When & Then
        mockMvc.perform(get("/api/nutrition/foods"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(0))
    }
}

