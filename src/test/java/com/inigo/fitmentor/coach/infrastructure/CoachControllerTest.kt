package com.inigo.fitmentor.coach.infrastructure

import com.inigo.arch.ArchApplication
//import com.inigo.arch.user.infrastructure.TestSecurityConfig
import com.inigo.fitmentor.coach.application.CreateCoach
import com.inigo.fitmentor.coach.application.FindAllCoaches
import com.inigo.fitmentor.coach.application.FindCoach
import com.inigo.fitmentor.coach.domain.Coach
import com.inigo.fitmentor.shared.domain.CoachId
import com.inigo.fitmentor.shared.domain.UserId
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ArchApplication::class]
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CoachControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean(relaxed = true)
    private lateinit var findCoach: FindCoach

    @MockkBean(relaxed = true)
    private lateinit var findAllCoaches: FindAllCoaches

    @MockkBean(relaxed = true)
    private lateinit var createCoach: CreateCoach

    @Test
    fun `should get coach successfully when coach exists`() {
        // Given
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val coach = Coach(
            id = CoachId(coachId),
            phonenumber = "123456789",
            presentation = "Experienced personal trainer",
            photo = "photo-url.jpg",
            user = UserId(userId),
            email = "random@email.com",
            username = "randomuser"
        )

        every { findCoach.execute(CoachId(coachId)) } returns coach

        // When & Then
        mockMvc.perform(
            get("/api/user/coaches/{id}", coachId)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(coachId.toString()))
            .andExpect(jsonPath("$.phonenumber").value("123456789"))
            .andExpect(jsonPath("$.presentation").value("Experienced personal trainer"))
            .andExpect(jsonPath("$.photo").value("photo-url.jpg"))
            .andExpect(jsonPath("$.user").value(userId.toString()))
            .andExpect(jsonPath("$.email").value("random@email.com"))
            .andExpect(jsonPath("$.username").value("randomuser"))

        verify(exactly = 1) { findCoach.execute(CoachId(coachId)) }
    }

    @Test
    fun `should return 404 when coach does not exist`() {
        // Given
        val coachId = UUID.randomUUID()

        every { findCoach.execute(CoachId(coachId)) } returns null

        // When & Then
        mockMvc.perform(
            get("/api/user/coaches/{id}", coachId)
        )
            .andExpect(status().isNotFound)

        verify(exactly = 1) { findCoach.execute(CoachId(coachId)) }
    }

    @Test
    fun `should create coach successfully with all fields`() {
        // Given
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        justRun { createCoach.execute(any()) }

        val requestBody = """
            {
                "id": "$coachId",
                "phonenumber": "987654321",
                "presentation": "Certified fitness coach",
                "user": "$userId",
                "photo": "coach-photo.jpg",
                "email": "random@email.com",
                "username": "randomuser"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/api/user/coaches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))

        verify(exactly = 1) { createCoach.execute(any()) }
    }

    @Test
    fun `should create coach successfully with only required fields`() {
        // Given
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        justRun { createCoach.execute(any()) }

        val requestBody = """
            {
                "id": "$coachId",
                "phonenumber": null,
                "presentation": null,
                "user": "$userId",
                "photo": null,
                "email": "random@email.com",
                "username": "randomuser"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/api/user/coaches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))

        verify(exactly = 1) { createCoach.execute(any()) }
    }

    @Test
    fun `should return 400 when creating coach with missing id`() {
        // Given
        val userId = UUID.randomUUID()

        val requestBody = """
            {
                "phonenumber": "987654321",
                "presentation": "Certified fitness coach",
                "user": "$userId",
                "photo": "coach-photo.jpg",
                "email": "random@email.com",
                "username": "randomuser"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/api/user/coaches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)

        verify(exactly = 0) { createCoach.execute(any()) }
    }

    @Test
    fun `should return 400 when creating coach with missing user`() {
        // Given
        val coachId = UUID.randomUUID()

        val requestBody = """
            {
                "id": "$coachId",
                "phonenumber": "987654321",
                "presentation": "Certified fitness coach",
                "photo": "coach-photo.jpg",
                "email": "random@email.com",
                "username": "randomuser"
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/api/user/coaches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isBadRequest)

        verify(exactly = 0) { createCoach.execute(any()) }
    }

    @Test
    fun `should throw exception when creating coach with invalid UUID format for id`() {
        // Given
        val userId = UUID.randomUUID()

        val requestBody = """
            {
                "id": "invalid-uuid",
                "phonenumber": "987654321",
                "presentation": "Certified fitness coach",
                "user": "$userId",
                "photo": "coach-photo.jpg",
                "email": "random@email.com",
                "username": "randomuser"
            }
        """.trimIndent()

        // When & Then - El código lanza IllegalArgumentException cuando el UUID es inválido
        // MockMvc propaga la excepción, así que la capturamos y verificamos
        try {
            mockMvc.perform(
                post("/api/user/coaches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            // Si llegamos aquí, el test debe fallar porque esperábamos una excepción
            assert(false) { "Se esperaba una excepción pero no se lanzó" }
        } catch (e: Exception) {
            // Verificamos que la excepción raíz sea IllegalArgumentException
            var cause: Throwable? = e
            var foundIllegalArgument = false
            while (cause != null) {
                if (cause is IllegalArgumentException) {
                    foundIllegalArgument = true
                    break
                }
                cause = cause.cause
            }
            assert(foundIllegalArgument) { "Se esperaba IllegalArgumentException en la cadena de causas" }
        }

        verify(exactly = 0) { createCoach.execute(any()) }
    }

    @Test
    fun `should get coach with null optional fields`() {
        // Given
        val coachId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val coach = Coach(
            id = CoachId(coachId),
            phonenumber = null,
            presentation = null,
            photo = null,
            user = UserId(userId),
            email = "random@email.com",
            username = "randomuser"
        )

        every { findCoach.execute(CoachId(coachId)) } returns coach

        // When & Then
        mockMvc.perform(
            get("/api/user/coaches/{id}", coachId)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(coachId.toString()))
            .andExpect(jsonPath("$.phonenumber").isEmpty)
            .andExpect(jsonPath("$.presentation").isEmpty)
            .andExpect(jsonPath("$.photo").isEmpty)
            .andExpect(jsonPath("$.user").value(userId.toString()))
            .andExpect(jsonPath("$.email").value("random@email.com"))
            .andExpect(jsonPath("$.username").value("randomuser"))

        verify(exactly = 1) { findCoach.execute(CoachId(coachId)) }
    }

    @Test
    fun `should get all coaches successfully`() {
        // Given
        val coach1 = Coach(
            id = CoachId(UUID.randomUUID()),
            phonenumber = "111111111",
            presentation = "Coach 1 presentation",
            photo = "photo1.jpg",
            user = UserId(UUID.randomUUID()),
            email = "coach1@email.com",
            username = "coach1"
        )

        val coach2 = Coach(
            id = CoachId(UUID.randomUUID()),
            phonenumber = "222222222",
            presentation = "Coach 2 presentation",
            photo = "photo2.jpg",
            user = UserId(UUID.randomUUID()),
            email = "coach2@email.com",
            username = "coach2"
        )

        every { findAllCoaches.execute() } returns listOf(coach1, coach2)

        // When & Then
        mockMvc.perform(
            get("/api/user/coaches")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("coach1"))
            .andExpect(jsonPath("$[0].presentation").value("Coach 1 presentation"))
            .andExpect(jsonPath("$[0].photo").value("photo1.jpg"))
            .andExpect(jsonPath("$[1].name").value("coach2"))
            .andExpect(jsonPath("$[1].presentation").value("Coach 2 presentation"))
            .andExpect(jsonPath("$[1].photo").value("photo2.jpg"))

        verify(exactly = 1) { findAllCoaches.execute() }
    }

    @Test
    fun `should return empty list when no coaches exist`() {
        // Given
        every { findAllCoaches.execute() } returns emptyList()

        // When & Then
        mockMvc.perform(
            get("/api/user/coaches")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        verify(exactly = 1) { findAllCoaches.execute() }
    }
}
