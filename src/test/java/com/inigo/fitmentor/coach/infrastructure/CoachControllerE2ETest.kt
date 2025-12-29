package com.inigo.fitmentor.coach.infrastructure

import com.inigo.arch.ArchApplication
import com.inigo.arch.user.infrastructure.TestSecurityConfig
import com.inigo.arch.user.infrastucture.jpa.UserJpa
import com.inigo.arch.user.infrastucture.jpa.UserJpaRepository
import com.inigo.arch.user.infrastucture.jpa.UserRepository
import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.UserId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootTest(classes = [ArchApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
@Transactional
class CoachControllerE2ETest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var coachRepository: CoachRepository

    @Autowired
    private lateinit var userRepository: UserJpaRepository

    @Test
    fun `E2E - should create and retrieve a coach successfully`() {
        // Given
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val createRequestBody = """
            {
                "id": "$coachId",
                "phonenumber": "555-5678",
                "presentation": "10 years experience in personal training",
                "user": "$userId",
                "photo": "profile-picture.jpg",
                "email": "random@email.com",
                "username": "randomuser"
            }
        """.trimIndent()

        // When - Create coach
        mockMvc.perform(
            post("/api/user/coaches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequestBody)
        )
            .andExpect(status().isOk)

        // Then - Retrieve coach
        mockMvc.perform(
            get("/api/user/coaches/{id}", coachId)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(coachId.toString()))
            .andExpect(jsonPath("$.phonenumber").value("555-5678"))
            .andExpect(jsonPath("$.presentation").value("10 years experience in personal training"))
            .andExpect(jsonPath("$.photo").value("profile-picture.jpg"))
            .andExpect(jsonPath("$.user").value(userId.toString()))
    }

    @Test
    fun `E2E - should retrieve an existing coach successfully`() {
        // Given - Create a coach directly in the repository
        val userJpa = UserJpa().apply {
            username = "randomuser"
            email = "random@email.com"
            password = "password" // Campo obligatorio
        }
        val user = userRepository.save(userJpa)
        val coachId = UUID.randomUUID()
        val userId = user.id
        var coach = Coach(
            id = CoachId(coachId),
            phonenumber = "999-8888",
            presentation = "Specialized in strength training",
            photo = "coach-photo.png",
            user = UserId(userId),
            email = "random@email.com",
            username = "randomuser"
        )

        coachRepository.save(CoachJpa.fromDomain(coach))

        // When & Then - Retrieve coach
        mockMvc.perform(
            get("/api/user/coaches/{id}", coachId)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(coachId.toString()))
            .andExpect(jsonPath("$.phonenumber").value("999-8888"))
            .andExpect(jsonPath("$.presentation").value("Specialized in strength training"))
            .andExpect(jsonPath("$.photo").value("coach-photo.png"))
            .andExpect(jsonPath("$.email").value("random@email.com"))
            .andExpect(jsonPath("$.username").value("randomuser"))
    }

    @Test
    fun `E2E - should return 404 for non-existing coach`() {
        // Given
        val nonExistingCoachId = UUID.randomUUID()

        // When & Then
        mockMvc.perform(
            get("/api/user/coaches/{id}", nonExistingCoachId)
        )
            .andExpect(status().isNotFound)
    }
}

