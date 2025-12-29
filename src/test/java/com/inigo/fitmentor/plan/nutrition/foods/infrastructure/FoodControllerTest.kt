package com.inigo.fitmentor.plan.nutrition.foods.infrastructure

import com.inigo.arch.ArchApplication
import com.inigo.arch.user.infrastructure.TestSecurityConfig
import com.inigo.fitmentor.plan.nutrition.foods.application.GetAllFoods
import com.inigo.fitmentor.plan.nutrition.foods.domain.Food
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ArchApplication::class]
)
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
@AutoConfigureMockMvc
class FoodControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean(relaxed = true)
    private lateinit var getAllFoods: GetAllFoods

    @Test
    fun `should return all foods successfully`() {
        // Given
        val food1 = Food(
            id = UUID.randomUUID(),
            name = "Chicken Breast",
            calPer100g = 165.0,
            proteinPer100g = 31.0,
            carbohydratePer100g = 0.0,
            fatPer100g = 3.6
        )
        val food2 = Food(
            id = UUID.randomUUID(),
            name = "Brown Rice",
            calPer100g = 111.0,
            proteinPer100g = 2.6,
            carbohydratePer100g = 23.0,
            fatPer100g = 0.9
        )

        every { getAllFoods.execute() } returns listOf(food1, food2)

        // When & Then
        mockMvc.perform(get("/api/nutrition/foods"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Chicken Breast"))
            .andExpect(jsonPath("$[0].calPer100g").value(165.0))
            .andExpect(jsonPath("$[0].proteinPer100g").value(31.0))
            .andExpect(jsonPath("$[0].carbohydratePer100g").value(0.0))
            .andExpect(jsonPath("$[0].fatPer100g").value(3.6))
            .andExpect(jsonPath("$[1].name").value("Brown Rice"))
            .andExpect(jsonPath("$[1].calPer100g").value(111.0))
            .andExpect(jsonPath("$[1].proteinPer100g").value(2.6))

        verify(exactly = 1) { getAllFoods.execute() }
    }

    @Test
    fun `should return empty list when no foods exist`() {
        // Given
        every { getAllFoods.execute() } returns emptyList()

        // When & Then
        mockMvc.perform(get("/api/nutrition/foods"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(0))

        verify(exactly = 1) { getAllFoods.execute() }
    }

    @Test
    fun `should return list with single food`() {
        // Given
        val food = Food(
            id = UUID.randomUUID(),
            name = "Salmon",
            calPer100g = 208.0,
            proteinPer100g = 20.0,
            carbohydratePer100g = 0.0,
            fatPer100g = 13.0
        )

        every { getAllFoods.execute() } returns listOf(food)

        // When & Then
        mockMvc.perform(get("/api/nutrition/foods"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("Salmon"))
            .andExpect(jsonPath("$[0].calPer100g").value(208.0))
            .andExpect(jsonPath("$[0].proteinPer100g").value(20.0))
            .andExpect(jsonPath("$[0].carbohydratePer100g").value(0.0))
            .andExpect(jsonPath("$[0].fatPer100g").value(13.0))

        verify(exactly = 1) { getAllFoods.execute() }
    }
}

